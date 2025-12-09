package relatorio.negocio.jdbc.financeiro;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.AgenteNegativacaoCobrancaContaReceberVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.PlanoDescontoContaReceberVO;
import negocio.comuns.financeiro.enumerador.TipoAgenteNegativacaoCobrancaContaReceberEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.FaixaDescontoProgressivo;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import relatorio.negocio.comuns.financeiro.InadimplenciaRelVO;
import relatorio.negocio.interfaces.financeiro.InadimplenciaRelInterfaceFacade;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@SuppressWarnings({"serial","deprecation"})
@Scope("singleton")
@Repository
@Lazy
public class InadimplenciaRel extends SuperRelatorio implements InadimplenciaRelInterfaceFacade {

	private static String idEntidade;
	private Double jurosPorcentagem;
	private Double multaPorcentagem;

	public InadimplenciaRel() {
		setIdEntidade("InadimplenciaRel");
	}

	@Override
	public List<InadimplenciaRelVO> gerarListaComDesconto(List<CursoVO> cursoVOs,List<UnidadeEnsinoVO> listaUnidadeEnsino, Boolean dataCompetencia,  TurmaVO turmaVO, MatriculaVO matriculaVO, 
			String ordenarPor,    Date dataInicio, Date dataFim, UsuarioVO usuario, PessoaVO responsavelFinanceiro, boolean filtrarAlunosSemEmail, FiltroRelatorioAcademicoVO filtroAcademicoVO, 
			Boolean trazerMatriculaSerasa, Boolean considerarUnidadeEnsinoFinanceira, Boolean imprimirApenasMatriculas, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO,CentroReceitaVO centroReceitaVO, AgenteNegativacaoCobrancaContaReceberVO agente, TipoAgenteNegativacaoCobrancaContaReceberEnum tipoAgente ,String situacaoRegistroCobranca) throws Exception {		
		StringBuilder sqlStr = new StringBuilder("");
	    sqlStr.append("select distinct ");
	    sqlStr.append(" (select string_agg(agentenegativacaocobrancacontareceber.nome, ', ') as agente from registroNegativacaoCobrancaContaReceberItem ");
	    sqlStr.append(" left join registroNegativacaoCobrancaContaReceber on registroNegativacaoCobrancaContaReceberItem.registroNegativacaoCobrancaContaReceber = registroNegativacaoCobrancaContaReceber.codigo  ");
	    sqlStr.append(" left join agentenegativacaocobrancacontareceber on agentenegativacaocobrancacontareceber.codigo = registroNegativacaoCobrancaContaReceber.agente ");
	    sqlStr.append(" where registroNegativacaoCobrancaContaReceberItem.contareceber = cr.codigo and registroNegativacaoCobrancaContaReceberItem.dataexclusao is null ) as agente, ");
	    sqlStr.append(" cr.codigo as codigocontareceber, ");
	// dados contareceber
		sqlStr.append("case when cr.parceiro is null then cr.matriculaaluno else cr.matriculaaluno end as matricula, ");
		sqlStr.append("case when pessoa.nome <> '' then pessoa.nome else p2.nome end as nome, ");
		sqlStr.append("case when cr.parceiro is null then case when responsavelFinanceiro.codigo is null then pessoa.celular else responsavelFinanceiro.celular end else p2.celular end as telcelular, ");
		sqlStr.append("case when cr.parceiro is null then case when responsavelFinanceiro.codigo is null then pessoa.email else responsavelFinanceiro.email end else p2.email end  as email, ");
		sqlStr.append("case when cr.parceiro is null then case when responsavelFinanceiro.codigo is null then pessoa.email2 else responsavelFinanceiro.email2 end else p2.email2 end  as email2, ");
		sqlStr.append("case when cr.parceiro is null then case when responsavelFinanceiro.codigo is null then pessoa.telefoneres else responsavelFinanceiro.telefoneres end else p2.telefoneres end  as telresidencial, ");
		sqlStr.append("case when cr.parceiro is null then case when responsavelFinanceiro.codigo is null then pessoa.cep else responsavelFinanceiro.cep end else p2.cep end  as cep1, ");
		sqlStr.append("case when cr.parceiro is null then case when responsavelFinanceiro.codigo is null then pessoa.endereco else responsavelFinanceiro.endereco end else p2.endereco end  as endereco1, ");
		sqlStr.append("case when cr.parceiro is null then case when responsavelFinanceiro.codigo is null then pessoa.numero else responsavelFinanceiro.numero end else p2.numero end  as numero1, ");
		sqlStr.append("case when cr.parceiro is null then case when responsavelFinanceiro.codigo is null then pessoa.setor else responsavelFinanceiro.setor end else p2.setor end  as bairro, ");
		sqlStr.append("case when cr.parceiro is null then case when responsavelFinanceiro.codigo is null then pessoa.complemento else  responsavelFinanceiro.complemento end  else p2.complemento end  as complemento1, ");
		sqlStr.append("case when cr.parceiro is null then case when responsavelFinanceiro.codigo is null then pessoa.cpf else responsavelFinanceiro.cpf end else p2.cpf end as cpf, ");
		sqlStr.append("case when cr.parceiro is null then case when responsavelFinanceiro.codigo is null then case when matricula.matricula is null then pessoa.seraza else matriculaserasa end else case when matricula.matricula is null then responsavelFinanceiro.seraza else matriculaserasa end end else p2.seraza end as serasa, ");
		sqlStr.append("case when cr.parceiro is null then case when responsavelFinanceiro.codigo is null then cidade.nome else cidadeRF.nome end else (c2.nome) end  as cidade1, ");
		sqlStr.append("case when cr.parceiro is null then case when responsavelFinanceiro.codigo is null then estado.sigla else estadoRF.sigla end else (e2.sigla)  end as estado1, ");
		sqlStr.append("parceiro.nome as parceiroNome, parceiro.telcomercial1 as parceiroTelComercial1, parceiro.cep as parceiroCep, ");
		sqlStr.append("parceiro.endereco as parceiroEndereco, parceiro.numero as parceiroNumero, parceiro.setor as parceiroSetor, ");
		sqlStr.append("parceiro.complemento as parceiroComplemento, pessoa.codigo as codigopessoa, t2.identificadorturma, ");
		sqlStr.append("unidadeensino.nome as unidadeEnsino, unidadeensinofinanceira.nome as unidadeEnsinoFinanceira, curso.nome as curso, turno.nome as turno, matriculaPeriodo.ano as ano, ");
		sqlStr.append("matriculaPeriodo.semestre as semestre, pessoa.email, pessoa.email2, ");
                
                
                // Linha abaixo substituiu a anterior dentro do processo de padronizacao de buscarmos todos os valores dos relatorios
                // boletos e painel gestor das mesmas variáveis. Esta variaval abaixo e atualizada via job
                sqlStr.append("cr.valorDescontoCalculadoPrimeiraFaixaDescontos as valordiaprimeirovencimento, ");
                
		sqlStr.append("responsavelFinanceiro.nome as responsavelFinanceiro_nome, responsavelFinanceiro.cpf as responsavelFinanceiro_cpf, ");
		// dados contareceber
		sqlStr.append("cr.tipoPessoa as \"cr.tipoPessoa\",  cr.data as \"cr.data\", cr.situacao as \"cr.situacao\", ");
		sqlStr.append("cr.dataVencimento as \"cr.dataVencimento\", cr.valor as \"cr.valor\", cr.pessoa as \"cr.pessoa\", cr.nrDocumento as \"cr.nrDocumento\", ");
		sqlStr.append("cr.tipoOrigem as \"cr.tipoOrigem\", cr.matriculaPeriodo as \"cr.matriculaPeriodo\", ");
		
		//Busca a Unidade de Ensino e a Unidade de Ensino Financeira, caso nao tenha, a UE sera a mesma da UE Financeira 
		sqlStr.append(" cr.unidadeensinofinanceira as \"cr.unidadeEnsinoFinanceira\", cr.unidadeEnsino as \"cr.unidadeEnsino\", ");
		
		sqlStr.append("cr.matriculaAluno as \"cr.matriculaAluno\", cr.funcionario as \"cr.funcionario\", cr.candidato as \"cr.candidato\", ");
		sqlStr.append("cr.convenio as \"cr.convenio\", cr.parceiro as \"cr.parceiro\", cr.valorRecebido as \"cr.valorRecebido\", cr.valorReceberCalculado as \"cr.valorReceberCalculado\", cr.juro as \"cr.juro\", ");
		sqlStr.append("cr.descricaoPagamento as \"cr.descricaoPagamento\", cr.codOrigem as \"cr.codOrigem\", cr.juroPorcentagem as \"cr.juroPorcentagem\", ");
		sqlStr.append("cr.multa as \"cr.multa\", coalesce(cr.acrescimo, 0.0) + coalesce(cr.valorindicereajusteporatraso, 0.0) + coalesce(cr.valorreajustediferencaparcelarecebidaouenviadaremessa, 0.0) as \"cr.acrescimo\", cr.multaPorcentagem as \"cr.multaPorcentagem\", cr.parcela as \"cr.parcela\", ");
		sqlStr.append("cr.descontoProgressivo as \"cr.descontoProgressivo\", cr.valorDescontoRecebido as \"cr.valorDescontoRecebido\",   cr.valorDescontoRateio as \"cr.valorDescontoRateio\", ");
		sqlStr.append("cr.tipoDesconto as \"cr.tipoDesconto\", cr.dataArquivoRemessa as \"cr.dataArquivoRemessa\", ");
		sqlStr.append("cr.descontoprogressivoutilizado as \"cr.descontoprogressivoutilizado\", cr.ordemConvenio as \"cr.ordemConvenio\", ");
		sqlStr.append("cr.OrdemConvenioValorCheio as \"cr.OrdemConvenioValorCheio\", cr.OrdemDescontoAluno as \"cr.OrdemDescontoAluno\", ");
		sqlStr.append("cr.OrdemDescontoAlunoValorCheio as \"cr.OrdemDescontoAlunoValorCheio\", cr.OrdemDescontoProgressivo as \"cr.OrdemDescontoProgressivo\", ");
		sqlStr.append("cr.OrdemDescontoProgressivoValorCheio as \"cr.OrdemDescontoProgressivoValorCheio\", cr.OrdemPlanoDesconto as \"cr.OrdemPlanoDesconto\", ");
		sqlStr.append("cr.OrdemPlanoDescontoValorCheio as \"cr.OrdemPlanoDescontoValorCheio\", cr.valorDesconto as \"cr.valorDesconto\", ");
		sqlStr.append("cr.justificativaDesconto as \"cr.justificativaDesconto\", cr.valorDescontoLancadoRecebimento as \"cr.valorDescontoLancadoRecebimento\", ");
		sqlStr.append("cr.valorCalculadoDescontoLancadoRecebimento as \"cr.valorCalculadoDescontoLancadoRecebimento\", ");
		sqlStr.append("cr.tipoDescontoLancadoRecebimento as \"cr.tipoDescontoLancadoRecebimento\", ");
		sqlStr.append("cr.valorDescontoAlunoJaCalculado as \"cr.valorDescontoAlunoJaCalculado\", ");
		sqlStr.append("cr.impressaoBoletoRealizada as \"cr.impressaoBoletoRealizada\", ");
		sqlStr.append("cr.valorDescontoCalculadoPrimeiraFaixaDescontos as \"cr.valorDescontoCalculadoPrimeiraFaixaDescontos\", ");
		sqlStr.append("cr.valorDescontoCalculadoSegundaFaixaDescontos as \"cr.valorDescontoCalculadoSegundaFaixaDescontos\", ");
		sqlStr.append("cr.valorDescontoCalculadoTerceiraFaixaDescontos as \"cr.valorDescontoCalculadoTerceiraFaixaDescontos\", ");
		sqlStr.append("cr.valorDescontoCalculadoQuartaFaixaDescontos as \"cr.valorDescontoCalculadoQuartaFaixaDescontos\", cr.updated as \"cr.updated\", ");
		sqlStr.append("cr.usaDescontoCompostoPlanoDesconto as \"cr.usaDescontoCompostoPlanoDesconto\", cr.descontoInstituicao as \"cr.descontoInstituicao\", ");
		sqlStr.append("cr.descontoConvenio as \"cr.descontoConvenio\", cr.valorDescontoProgressivo as \"cr.valorDescontoProgressivo\", cr.nossonumero as \"cr.nossonumero\", ");
		// dados planodescontocontareceber
		sqlStr.append("pdcr.codigo as \"pdcr.codigo\", pdcr.contaReceber as \"pdcr.contaReceber\", pdcr.planoDesconto as \"pdcr.planoDesconto\", ");
		sqlStr.append("pdcr.tipoItemPlanoFinanceiro as \"pdcr.tipoItemPlanoFinanceiro\", pdcr.convenio as \"pdcr.convenio\", ");
		sqlStr.append("pdcr.valorutilizadorecebimento as \"pdcr.valorutilizadorecebimento\", ");
		// dados planodesconto
		sqlStr.append("pd.codigo as \"pd.codigo\", pd.nome as \"pd.nome\", pd.percDescontoParcela as \"pd.percDescontoParcela\", ");
		sqlStr.append("pd.percDescontoMatricula as \"pd.percDescontoMatricula\", pd.requisitos as \"pd.requisitos\", pd.descricao as \"pd.descricao\", ");
		sqlStr.append("pd.somente1PeriodoLetivoParcela as \"pd.somente1PeriodoLetivoParcela\", pd.somente1PeriodoLetivoMatricula as \"pd.somente1PeriodoLetivoMatricula\", ");
		sqlStr.append("pd.tipoDescontoParcela as \"pd.tipoDescontoParcela\", pd.tipoDescontoMatricula as \"pd.tipoDescontoMatricula\", ");
		sqlStr.append("pd.diasValidadeVencimento as \"pd.diasValidadeVencimento\", pd.ativo as \"pd.ativo\", pd.dataAtivacao as \"pd.dataAtivacao\", ");
		sqlStr.append("pd.dataInativacao as \"pd.dataInativacao\", pd.responsavelAtivacao as \"pd.responsavelAtivacao\", ");
		sqlStr.append("pd.responsavelInativacao as \"pd.responsavelInativacao\", pd.descontoValidoAteDataVencimento as \"pd.descontoValidoAteDataVencimento\", ");
		sqlStr.append("pd.aplicarSobreValorCheio as \"pd.aplicarSobreValorCheio\", pd.utilizarDiaUtil as \"pd.utilizarDiaUtil\", ");
		sqlStr.append(" pd.utilizarDiaFixo as \"pd.utilizarDiaFixo\",  pd.utilizarAvancoDiaUtil as \"pd.utilizarAvancoDiaUtil\", ");
		// dados usuarioativacao
		sqlStr.append("ua.codigo as \"ua.codigo\", ua.nome as \"ua.nome\", ua.username as \"ua.username\", ua.tipoUsuario as \"ua.tipoUsuario\", ua.pessoa as \"ua.pessoa\", ");
		sqlStr.append("ua.dataUltimoAcesso as \"ua.dataUltimoAcesso\", ua.parceiro as \"ua.parceiro\", pa.codigo as \"pa.codigo\", pa.nome as \"pa.nome\", ");
		// dados usuarioinativacao
		sqlStr.append("ui.codigo as \"ui.codigo\", ui.nome as \"ui.nome\", ui.username as \"ui.username\", ui.tipoUsuario as \"ui.tipoUsuario\", ui.pessoa as \"ui.pessoa\", ");
		sqlStr.append("ui.dataUltimoAcesso as \"ui.dataUltimoAcesso\", ui.parceiro as \"ui.parceiro\", pi.codigo as \"pi.codigo\", pi.nome as \"pi.nome\", ");
		// dados convenio
		sqlStr.append("c.codigo as \"c.codigo\", c.descricao as \"c.descricao\", c.dataAssinatura as \"c.dataAssinatura\", c.parceiro as \"c.parceiro\", ");
		sqlStr.append("c.ativo as \"c.ativo\", c.cobertura as \"c.cobertura\", c.preRequisitos as \"c.preRequisitos\", c.dataInicioVigencia as \"c.dataInicioVigencia\", ");
		sqlStr.append("c.dataFinalVigencia as \"c.dataFinalVigencia\", c.descontoMatricula as \"c.descontoMatricula\", ");
		sqlStr.append("c.tipoDescontoMatricula as \"c.tipoDescontoMatricula\", c.descontoParcela as \"c.descontoParcela\", ");
		sqlStr.append("c.tipoDescontoParcela as \"c.tipoDescontoParcela\", c.bolsaCusteadaParceiroMatricula as \"c.bolsaCusteadaParceiroMatricula\", ");
		sqlStr.append("c.tipoBolsaCusteadaParceiroMatricula as \"c.tipoBolsaCusteadaParceiroMatricula\", ");
		sqlStr.append("c.bolsaCusteadaParceiroParcela as \"c.bolsaCusteadaParceiroParcela\", c.tipoBolsaCusteadaParceiroParcela as \"c.tipoBolsaCusteadaParceiroParcela\", ");
		sqlStr.append("c.formaRecebimentoParceiro as \"c.formaRecebimentoParceiro\", c.diaBaseRecebimentoParceiro as \"c.diaBaseRecebimentoParceiro\", ");
		sqlStr.append("c.requisitante as \"c.requisitante\", c.dataRequisicao as \"c.dataRequisicao\", c.responsavelAutorizacao as \"c.responsavelAutorizacao\", ");
		sqlStr.append("c.dataAutorizacao as \"c.dataAutorizacao\", c.responsavelFinalizacao as \"c.responsavelFinalizacao\", ");
		sqlStr.append("c.dataFinalizacao as \"c.dataFinalizacao\", c.situacao as \"c.situacao\", c.validoParaTodoCurso as \"c.validoParaTodoCurso\", ");
		sqlStr.append("c.validoParaTodaUnidadeEnsino as \"c.validoParaTodaUnidadeEnsino\", c.validoParaTodoTurno as \"c.validoParaTodoTurno\", ");
		sqlStr.append("c.periodoIndeterminado as \"c.periodoIndeterminado\", c.dataAtivacao as \"c.dataAtivacao\", c.dataInativacao as \"c.dataInativacao\", ");
		sqlStr.append("c.responsavelAtivacao as \"c.responsavelAtivacao\", c.responsavelInativacao as \"c.responsavelInativacao\", ");
		sqlStr.append("c.descontoProgressivoParceiro as \"c.descontoProgressivoParceiro\", c.descontoProgressivoAluno as \"c.descontoProgressivoAluno\", ");
		sqlStr.append("c.possuiDescontoAntecipacao as \"c.possuiDescontoAntecipacao\", c.calculadoEmCimaValorLiquido as \"c.calculadoEmCimaValorLiquido\", ");
		sqlStr.append("c.aplicarDescontoProgressivoMatricula as \"c.aplicarDescontoProgressivoMatricula\", ");
		sqlStr.append("c.aplicarDescontoProgressivoMatriculaParceiro as \"c.aplicarDescontoProgressivoMatriculaParceiro\", ");
		// dados descontoprogressivo_contareceber
		sqlStr.append("descontoprogressivo.codigo as \"descontoprogressivo.codigo\", descontoprogressivo.nome as \"descontoprogressivo.nome\", descontoprogressivo.diaLimite1 as \"descontoprogressivo.diaLimite1\", ");
		sqlStr.append("descontoprogressivo.percDescontoLimite1 as \"descontoprogressivo.percDescontoLimite1\", descontoprogressivo.diaLimite2 as \"descontoprogressivo.diaLimite2\", ");
		sqlStr.append("descontoprogressivo.percDescontoLimite2 as \"descontoprogressivo.percDescontoLimite2\", descontoprogressivo.diaLimite3 as \"descontoprogressivo.diaLimite3\", ");
		sqlStr.append("descontoprogressivo.percDescontoLimite3 as \"descontoprogressivo.percDescontoLimite3\", descontoprogressivo.diaLimite4 as \"descontoprogressivo.diaLimite4\", ");
		sqlStr.append("descontoprogressivo.percDescontoLimite4 as \"descontoprogressivo.percDescontoLimite4\", descontoprogressivo.valorDescontoLimite1 as \"descontoprogressivo.valorDescontoLimite1\", ");
		sqlStr.append("descontoprogressivo.valorDescontoLimite2 as \"descontoprogressivo.valorDescontoLimite2\", descontoprogressivo.valorDescontoLimite3 as \"descontoprogressivo.valorDescontoLimite3\", ");
		sqlStr.append("descontoprogressivo.valorDescontoLimite4 as \"descontoprogressivo.valorDescontoLimite4\", descontoprogressivo.ativado as \"descontoprogressivo.ativado\", descontoprogressivo.dataAtivacao as \"descontoprogressivo.dataAtivacao\", ");
		sqlStr.append("descontoprogressivo.responsavelAtivacao as \"descontoprogressivo.responsavelAtivacao\", descontoprogressivo.dataInativacao as \"descontoprogressivo.dataInativacao\", ");
		sqlStr.append("descontoprogressivo.responsavelInativacao as \"descontoprogressivo.responsavelInativacao\", ");
		sqlStr.append("descontoprogressivo.utilizarDiaFixo as \"descontoprogressivo.utilizarDiaFixo\", descontoprogressivo.utilizarDiaUtil as \"descontoprogressivo.utilizarDiaUtil\", ");
		// dados descontoprogressivoaluno
		sqlStr.append("dpa.codigo as \"dpa.codigo\", dpa.nome as \"dpa.nome\", dpa.diaLimite1 as \"dpa.diaLimite1\", ");
		sqlStr.append("dpa.percDescontoLimite1 as \"dpa.percDescontoLimite1\", dpa.diaLimite2 as \"dpa.diaLimite2\", ");
		sqlStr.append("dpa.percDescontoLimite2 as \"dpa.percDescontoLimite2\", dpa.diaLimite3 as \"dpa.diaLimite3\", ");
		sqlStr.append("dpa.percDescontoLimite3 as \"dpa.percDescontoLimite3\", dpa.diaLimite4 as \"dpa.diaLimite4\", ");
		sqlStr.append("dpa.percDescontoLimite4 as \"dpa.percDescontoLimite4\", dpa.valorDescontoLimite1 as \"dpa.valorDescontoLimite1\", ");
		sqlStr.append("dpa.valorDescontoLimite2 as \"dpa.valorDescontoLimite2\", dpa.valorDescontoLimite3 as \"dpa.valorDescontoLimite3\", ");
		sqlStr.append("dpa.valorDescontoLimite4 as \"dpa.valorDescontoLimite4\", dpa.ativado as \"dpa.ativado\", dpa.dataAtivacao as \"dpa.dataAtivacao\", ");
		sqlStr.append("dpa.responsavelAtivacao as \"dpa.responsavelAtivacao\", dpa.dataInativacao as \"dpa.dataInativacao\", ");
		sqlStr.append("dpa.responsavelInativacao as \"dpa.responsavelInativacao\", ");
		sqlStr.append("dpa.utilizarDiaFixo as \"dpa.utilizarDiaFixo\", dpa.utilizarDiaUtil as \"dpa.utilizarDiaUtil\", ");
		// dados descontoprogressivoparceiro
		sqlStr.append("dpp.codigo as \"dpp.codigo\", dpp.nome as \"dpp.nome\", dpp.diaLimite1 as \"dpp.diaLimite1\", ");
		sqlStr.append("dpp.percDescontoLimite1 as \"dpp.percDescontoLimite1\", dpp.diaLimite2 as \"dpp.diaLimite2\", ");
		sqlStr.append("dpp.percDescontoLimite2 as \"dpp.percDescontoLimite2\", dpp.diaLimite3 as \"dpp.diaLimite3\", ");
		sqlStr.append("dpp.percDescontoLimite3 as \"dpp.percDescontoLimite3\", dpp.diaLimite4 as \"dpp.diaLimite4\", ");
		sqlStr.append("dpp.percDescontoLimite4 as \"dpp.percDescontoLimite4\", dpp.valorDescontoLimite1 as \"dpp.valorDescontoLimite1\", ");
		sqlStr.append("dpp.valorDescontoLimite2 as \"dpp.valorDescontoLimite2\", dpp.valorDescontoLimite3 as \"dpp.valorDescontoLimite3\", ");
		sqlStr.append("dpp.valorDescontoLimite4 as \"dpp.valorDescontoLimite4\", dpp.ativado as \"dpp.ativado\", dpp.dataAtivacao as \"dpp.dataAtivacao\", ");
		sqlStr.append("dpp.responsavelAtivacao as \"dpp.responsavelAtivacao\", dpp.dataInativacao as \"dpp.dataInativacao\", ");
		sqlStr.append("dpp.responsavelInativacao as \"dpp.responsavelInativacao\", ");
		sqlStr.append("dpp.utilizarDiaFixo as \"dpp.utilizarDiaFixo\", dpp.utilizarDiaUtil as \"dpp.utilizarDiaUtil\", ");

		sqlStr.append("cr.dataprocessamentovalorreceber as \"cr.dataprocessamentovalorreceber\", cr.valorrecebercalculado as \"cr.valorrecebercalculado\", ");
		sqlStr.append("cr.valorjurocalculado as \"cr.valorjurocalculado\", cr.valormultacalculado as \"cr.valormultacalculado\", ");
		sqlStr.append("cr.valordescontocalculado as \"cr.valordescontocalculado\" ,CASE WHEN p2.codigo is not null AND P2.datanasc is not null THEN extract(year from age(p2.datanasc::date))::text  ELSE NULL END  as \"idadeAluno\" ");
		sqlStr.append("from contareceber cr ");
		
		//Buscando a unidade financeira
		sqlStr.append("inner join unidadeensino unidadeensinofinanceira on unidadeensinofinanceira.codigo = cr.unidadeensinofinanceira ");
		sqlStr.append("inner join unidadeensino on unidadeensino.codigo = cr.unidadeensino ");
		
		sqlStr.append("left join descontoprogressivo on descontoprogressivo.codigo = cr.descontoprogressivo ");
		sqlStr.append("left join matricula on cr.matriculaaluno = matricula.matricula ");
		sqlStr.append("left join matriculaperiodo on matricula.matricula = matriculaperiodo.matricula and case when cr.matriculaperiodo is null then matriculaperiodo.codigo = (select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by (mp.ano||'/'||mp.semestre )desc limit 1 ) else matriculaperiodo.codigo = cr.matriculaperiodo end ");
		
		sqlStr.append("left join curso on curso.codigo = matricula.curso ");
		sqlStr.append("left join turma t2 on t2.codigo = matriculaperiodo.turma ");
		if (filtroRelatorioFinanceiroVO.getTipoOrigemBolsaCusteadaConvenio() ) {
			sqlStr.append("left join pessoa on pessoa.codigo = cr.pessoa ");
		} else {
			sqlStr.append("inner join pessoa on pessoa.codigo = cr.pessoa ");
		}
		sqlStr.append("left join pessoa p2 on p2.codigo = matricula.aluno ");
		sqlStr.append("left join turno on turno.codigo = matricula.turno ");
		sqlStr.append("left join cidade on cidade.codigo = pessoa.cidade ");
		sqlStr.append("left join estado on estado.codigo = cidade.estado ");
		sqlStr.append("left JOIN parceiro ON cr.parceiro = parceiro.codigo ");
		sqlStr.append("left join cidade c2 on c2.codigo = parceiro.cidade ");
		sqlStr.append("left join estado e2 on e2.codigo = c2.estado ");

		sqlStr.append("LEFT JOIN PlanoDescontoContaReceber pdcr ON pdcr.contareceber = cr.codigo ");
		sqlStr.append("LEFT JOIN PlanoDesconto pd ON pd.codigo = pdcr.planodesconto ");
		sqlStr.append("LEFT JOIN Usuario ua ON ua.codigo = pd.responsavelativacao ");
		sqlStr.append("LEFT JOIN Pessoa pa ON pa.codigo = ua.pessoa ");
		sqlStr.append("LEFT JOIN Usuario ui ON ui.codigo = pd.responsavelinativacao ");
		sqlStr.append("LEFT JOIN Pessoa pi ON pi.codigo = ui.pessoa ");
		sqlStr.append("LEFT JOIN Convenio c ON c.codigo = pdcr.convenio ");
		sqlStr.append("LEFT JOIN DescontoProgressivo dpa ON dpa.codigo = c.descontoprogressivoaluno ");
		sqlStr.append("LEFT JOIN DescontoProgressivo dpp ON dpp.codigo = c.descontoprogressivoparceiro ");
		sqlStr.append("left join pessoa responsavelFinanceiro on responsavelFinanceiro.codigo = cr.responsavelFinanceiro ");
		sqlStr.append("left join cidade cidadeRF on cidadeRF.codigo = responsavelFinanceiro.cidade ");
		sqlStr.append("left join estado estadoRF on estadoRF.codigo = cidadeRF.estado ");

		if(Uteis.isAtributoPreenchido(agente.getCodigo())){
			sqlStr.append("left join registroNegativacaoCobrancaContaReceberItem regNegItem on regNegItem.contareceber = cr.codigo and regNegItem.dataexclusao is null ");
			sqlStr.append("left join registroNegativacaoCobrancaContaReceber regNeg on regNegItem.registroNegativacaoCobrancaContaReceber = regNeg.codigo ");
			sqlStr.append("left join agentenegativacaocobrancacontareceber agente on agente.codigo = regNeg.agente ");
		}
		
		
		
		sqlStr.append("where cr.situacao = 'AR' and cr.valor > 0 ");

		if(Uteis.isAtributoPreenchido(centroReceitaVO.getCodigo())){
			sqlStr.append(" AND cr.centroreceita = ").append(centroReceitaVO.getCodigo().intValue()).append(" ");
		}
		if(Uteis.isAtributoPreenchido(agente.getCodigo())){
			sqlStr.append(" AND regNeg.agente = ").append(agente.getCodigo().intValue()).append(" ");
			sqlStr.append(" AND regNeg.tipoAgente = '").append(tipoAgente).append("' ");
		}
		if (!listaUnidadeEnsino.isEmpty()) {
			
			//Considera a Unidade de Ensino Financeira
			if(considerarUnidadeEnsinoFinanceira) {
				sqlStr.append(" and cr.unidadeensinofinanceira in (");				
			} else {
				sqlStr.append(" and cr.unidadeensino in (");
			}
			
			for (UnidadeEnsinoVO ue : listaUnidadeEnsino) {
				if (ue.getFiltrarUnidadeEnsino()) {
					sqlStr.append(ue.getCodigo() + ", ");
				}
			}
			sqlStr.append("0) ");
		}
		sqlStr.append(" AND ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroAcademicoVO, "matriculaperiodo"));
		sqlStr.append(" AND ").append(adicionarFiltroTipoOrigemContaReceber(filtroRelatorioFinanceiroVO, "cr"));
		if (Uteis.isAtributoPreenchido(cursoVOs)) {
			int x = 0;
			for (CursoVO cursoVO : cursoVOs) {
				if(cursoVO.getFiltrarCursoVO()){
				if (x > 0) {
					sqlStr.append(", ");
				}else{
					sqlStr.append(" AND curso.codigo  IN (");
				}
				sqlStr.append(cursoVO.getCodigo());				
				x++;
				}
			}
			if (x > 0) {
				sqlStr.append(" ) ");
			}
		}

		    if(turmaVO.getCodigo() != null && turmaVO.getCodigo() != 0) {
			  sqlStr.append(" and matriculaperiodo.codigo > 0 and t2.codigo = ");
			  sqlStr.append(turmaVO.getCodigo());
			}
			if (matriculaVO.getMatricula() != null && !matriculaVO.getMatricula().equals("")) {
				sqlStr.append(" and upper(matricula.matricula) = '").append(matriculaVO.getMatricula().toUpperCase()).append("' ");
			}
			if (!filtroRelatorioFinanceiroVO.getTipoOrigemBolsaCusteadaConvenio()) {
				sqlStr.append(" and (parceiro.codigo is null) and (pessoa.codigo is not null or responsavelFinanceiro.codigo is not null) ");
			}
			if (responsavelFinanceiro != null && responsavelFinanceiro.getCodigo() > 0) {
				sqlStr.append(" and responsavelFinanceiro.codigo = ").append(responsavelFinanceiro.getCodigo());
			}
			if(dataCompetencia != null && dataCompetencia) {
				sqlStr.append(" AND ").append(realizarGeracaoWherePeriodoConsiderandoMesAno(dataInicio, dataFim, "cr.datacompetencia"));
			}else {
				if (dataInicio != null) {
					sqlStr.append(" AND cr.datavencimento between '").append(Uteis.getDataBD0000(dataInicio)).append("' and '").append(Uteis.getDataBD2359(dataFim)).append("' ");
				} else {
					sqlStr.append(" AND cr.datavencimento < '").append(Uteis.getDataBD2359(dataFim)).append("'");
				}
			}
			if (filtrarAlunosSemEmail) {
				sqlStr.append(" and (pessoa.email <> '' or responsavelfinanceiro.email <> '' or p2.email <> '' or pessoa.email2 <> '' or responsavelfinanceiro.email2 <> '' or p2.email2 <> '') ");
			}

			if (trazerMatriculaSerasa) {
				sqlStr.append(" AND matricula.matriculaserasa = true ");
			}
			if (imprimirApenasMatriculas) {
				sqlStr.append(" AND cr.tipoorigem = 'MAT' ");
			}
			sqlStr.append(adicionarFiltroRegistroCobranca(situacaoRegistroCobranca, dataInicio, dataFim, dataCompetencia));
			
			if (ordenarPor.equals("TU")) {
				sqlStr.append(" order by curso.nome, t2.identificadorturma, nome, cr.codigo ");
			} else {
				sqlStr.append(" order by nome, cr.codigo ");
			}		
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());		
			return montarDadosConsultaRelatorioComDesconto(tabelaResultado, false,  usuario);
		 
	}
	
	private StringBuilder adicionarFiltroRegistroCobranca(String situacaoRegistroCobranca, Date dataInicio, Date dataFim, Boolean dataCompetencia) {
		StringBuilder sqlStr = new StringBuilder("");
		if(Uteis.isAtributoPreenchido(situacaoRegistroCobranca)) {	
			if( situacaoRegistroCobranca.equalsIgnoreCase(FiltroRelatorioFinanceiroVO.FiltroRegistroCobranca.SIMDEACORDOCOMPERIODOFILTRADO.getValor())
					|| situacaoRegistroCobranca.equalsIgnoreCase(FiltroRelatorioFinanceiroVO.FiltroRegistroCobranca.SIMINDEPENDENTEDOPERIODOFILTRADO.getValor())) {
				sqlStr.append(" and (  exists ( ");
			}else {
				sqlStr.append(" and (  not exists ( ");
			}
			sqlStr.append(" select registronegativacaocobrancacontareceberitem.codigo from contareceber ");
			sqlStr.append(" inner join registronegativacaocobrancacontareceberitem ");
			sqlStr.append(" on contareceber.codigo = registronegativacaocobrancacontareceberitem.contareceber ");
			sqlStr.append(" where registronegativacaocobrancacontareceberitem.dataExclusao is null ");				
			if( situacaoRegistroCobranca.equalsIgnoreCase(FiltroRelatorioFinanceiroVO.FiltroRegistroCobranca.SIMDEACORDOCOMPERIODOFILTRADO.getValor())
					|| situacaoRegistroCobranca.equalsIgnoreCase(FiltroRelatorioFinanceiroVO.FiltroRegistroCobranca.NAODEACORDOCOMPERIODOFILTRADO.getValor())) {
				if(dataCompetencia != null && dataCompetencia) {
					sqlStr.append(" and ").append(realizarGeracaoWherePeriodoConsiderandoMesAno(dataInicio, dataFim, "contareceber.datacompetencia"));
				}else {
				sqlStr.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataFim, "contareceber.datavencimento", false));
				}
			}
			sqlStr.append(" and ((cr.tipopessoa = 'AL' and contareceber.tipopessoa = 'AL' and cr.matriculaaluno = contareceber.matriculaaluno) ");
			sqlStr.append(" or (cr.tipopessoa = 'PA' and contareceber.tipopessoa = 'PA' and cr.parceiro = contareceber.parceiro) ");
			sqlStr.append(" or (cr.tipopessoa = 'FO' and contareceber.tipopessoa = 'FO' and cr.fornecedor = contareceber.fornecedor) ");
			sqlStr.append(" or (cr.tipopessoa = 'CA' and contareceber.tipopessoa = 'CA' and cr.candidato = contareceber.candidato) ");
			sqlStr.append(" or (cr.tipopessoa = 'FU' and contareceber.tipopessoa = 'FU' and cr.funcionario = contareceber.funcionario) ");
			sqlStr.append(" or (cr.tipopessoa = 'RF' and contareceber.tipopessoa = 'RF' and cr.responsavelfinanceiro = contareceber.responsavelfinanceiro) ");
			sqlStr.append(" or (cr.tipopessoa = 'RF' and contareceber.tipopessoa = 'RF' and cr.matriculaaluno = contareceber.matriculaaluno) ) ");
			sqlStr.append(" limit 1))  ");
		}
		return sqlStr;
	}


	public Integer gerarQuantidadeAlunosInadimplentes(List<CursoVO> cursoVOs,List<UnidadeEnsinoVO> listaUnidadeEnsino, Boolean dataCompetencia, TurmaVO turmaVO, MatriculaVO matriculaVO,  Date dataInicio, Date dataFim, UsuarioVO usuario, PessoaVO responsavelFinanceiro, boolean filtrarAlunosSemEmail, FiltroRelatorioAcademicoVO filtroAcademicoVO, Boolean trazerMatriculaSerasa, Boolean considerarUnidadeFinanceira, Boolean imprimirApenasMatriculas, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO,CentroReceitaVO centroReceitaVO, String situacaoRegristroCobranca) throws Exception {	
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("SELECT ");
		sqlStr.append(" COUNT (DISTINCT CASE WHEN  cr.tipopessoa = 'PA'              THEN cr.parceiro   END) + ");
		sqlStr.append(" COUNT (DISTINCT CASE WHEN  cr.tipopessoa = 'FO'              THEN cr.fornecedor END) + ");
		sqlStr.append(" COUNT (DISTINCT CASE WHEN  cr.tipopessoa NOT IN ('PA','FO')  THEN cr.pessoa     END) AS numeroAlunosInadimplentes ");
		sqlStr.append("FROM contareceber cr  ");
		
		if(considerarUnidadeFinanceira)
			sqlStr.append(" INNER JOIN unidadeensino on unidadeensino.codigo = cr.unidadeensinofinanceira ");
		else
			sqlStr.append(" INNER JOIN unidadeensino on unidadeensino.codigo = cr.unidadeensino ");
		
		sqlStr.append(" LEFT JOIN descontoprogressivo on descontoprogressivo.codigo = cr.descontoprogressivo ");
		sqlStr.append(" LEFT JOIN matricula on cr.matriculaaluno = matricula.matricula ");
		sqlStr.append(" LEFT JOIN matriculaperiodo on matricula.matricula = matriculaperiodo.matricula and case when cr.matriculaperiodo is null then matriculaperiodo.codigo = (select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by (mp.ano||'/'||mp.semestre )desc limit 1 ) else matriculaperiodo.codigo = cr.matriculaperiodo end ");
		sqlStr.append(" LEFT JOIN pessoa responsavelFinanceiro on responsavelFinanceiro.codigo = cr.responsavelFinanceiro ");
		sqlStr.append(" LEFT JOIN pessoa on pessoa.codigo = cr.pessoa ");
		sqlStr.append(" INNER JOIN curso on curso.codigo = matricula.curso ");
		sqlStr.append(" INNER JOIN turma t2 on t2.codigo = matriculaperiodo.turma ");
		sqlStr.append(" LEFT JOIN pessoa p2 on p2.codigo = matricula.aluno ");
		sqlStr.append(" LEFT JOIN parceiro ON cr.parceiro = parceiro.codigo ");
		sqlStr.append("WHERE cr.situacao = 'AR' and cr.valor > 0 ");
		if(Uteis.isAtributoPreenchido(centroReceitaVO.getCodigo())){
			sqlStr.append(" AND cr.centroreceita = ").append(centroReceitaVO.getCodigo().intValue()).append(" ");
		}
		if (!listaUnidadeEnsino.isEmpty()) {
			
			if(considerarUnidadeFinanceira)
				sqlStr.append("AND cr.unidadeensinofinanceira in (");
			else
				sqlStr.append("AND cr.unidadeensino in (");
			
			for (UnidadeEnsinoVO ue : listaUnidadeEnsino) {
				if (ue.getFiltrarUnidadeEnsino()) {
					sqlStr.append(ue.getCodigo() + ", ");
				}
			}
			sqlStr.append("0) ");
		}
		if (Uteis.isAtributoPreenchido(cursoVOs)) {
			int x = 0;
			for (CursoVO cursoVO : cursoVOs) {
				if(cursoVO.getFiltrarCursoVO()){
				if (x > 0) {
					sqlStr.append(", ");
				}else{
					sqlStr.append(" AND curso.codigo  IN (");
				}
				sqlStr.append(cursoVO.getCodigo());
				x++;
				}
			}
			if (x > 0) {
				sqlStr.append(" ) ");
			}
		}
		sqlStr.append(" AND ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroAcademicoVO, "matriculaperiodo"));
		sqlStr.append(" AND ").append(adicionarFiltroTipoOrigemContaReceber(filtroRelatorioFinanceiroVO, "cr"));		
		if (turmaVO.getCodigo() != null && turmaVO.getCodigo() != 0) {
			sqlStr.append(" AND matriculaperiodo.codigo > 0 AND t2.codigo = ");
			sqlStr.append(turmaVO.getCodigo());
		}
		if (matriculaVO.getMatricula() != null && !matriculaVO.getMatricula().equals("")) {
			sqlStr.append(" AND UPPER(matricula.matricula) = '").append(matriculaVO.getMatricula().toUpperCase()).append("' ");
		}
		if (!filtroRelatorioFinanceiroVO.getTipoOrigemBolsaCusteadaConvenio()) {
			sqlStr.append(" AND (parceiro.codigo IS NULL) AND (pessoa.codigo IS NOT NULL OR responsavelFinanceiro.codigo IS NOT NULL) ");
		}
		if (responsavelFinanceiro != null && responsavelFinanceiro.getCodigo() > 0) {
			sqlStr.append(" AND responsavelFinanceiro.codigo = ").append(responsavelFinanceiro.getCodigo());
		}
		if(dataCompetencia != null && dataCompetencia) {
			sqlStr.append(" AND ").append(realizarGeracaoWherePeriodoConsiderandoMesAno(dataInicio, dataFim, "cr.datacompetencia"));
		}else {
			if (dataInicio != null) {
				sqlStr.append(" AND cr.datavencimento between '").append(Uteis.getDataBD0000(dataInicio)).append("' and '").append(Uteis.getDataBD2359(dataFim)).append("' ");
			} else {
				sqlStr.append(" AND cr.datavencimento < '").append(Uteis.getDataBD2359(dataFim)).append("'");
			}
		}
		if (filtrarAlunosSemEmail) {
			sqlStr.append(" AND (pessoa.email <> '' OR responsavelfinanceiro.email <> '' OR p2.email <> '' OR pessoa.email2 <> '' OR responsavelfinanceiro.email2 <> '' OR p2.email2 <> '') ");
		}	
		if(trazerMatriculaSerasa){
			sqlStr.append(" AND matricula.matriculaserasa = true ");				
		}
		if(imprimirApenasMatriculas){
			sqlStr.append(" AND cr.tipoorigem = 'MAT' ");
		}
		sqlStr.append(" and cr.datavencimento < current_date");
		sqlStr.append(adicionarFiltroRegistroCobranca(situacaoRegristroCobranca, dataInicio, dataFim, dataCompetencia));
		return getConexao().getJdbcTemplate().queryForInt(sqlStr.toString());
	}

	public List<InadimplenciaRelVO> gerarLista(List<UnidadeEnsinoVO> listaUnidadeEnsino, Boolean dataCompetencia, UnidadeEnsinoCursoVO unidadeEnsinoCursoVO, TurmaVO turmaVO, Date dataInicio, Date dataFim, String ordenacao, ConfiguracaoFinanceiroVO configuracao, PessoaVO responsavelFinanceiro,String situacaoRegristroCobranca) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append("case when cr.parceiro is null then cr.matriculaaluno else 'Parceiro' end as matricula, ");
		sql.append("case when p.nome <> '' then p.nome else parceiro.nome end as nome1, ");
		sql.append("case when cr.parceiro is null then case when responsavelFinanceiro.codigo is null then p.telefoneres else responsavelFinanceiro.telefoneres end else parceiro.telcomercial1 end  as telresidencial1, ");
		sql.append("case when cr.parceiro is null then case when responsavelFinanceiro.codigo is null then p.email else responsavelFinanceiro.email end else parceiro.email end as email1, ");
		sql.append("case when cr.parceiro is null then case when responsavelFinanceiro.codigo is null then case when m.matricula is null then p.seraza else matriculaserasa end else case when m.matricula is null then responsavelFinanceiro.seraza else matriculaserasa end end end as serasa, ");
		sql.append("t.identificadorTurma AS turma, p.cpf AS cpf, p.celular AS telcelular, c.nome AS curso,  cr.tipoorigem AS origem, ");
		sql.append("cr.dataVencimento AS dataVencimento, cr.valor AS valor, cr.multaporcentagem As multaPorcentagem, cr.multa As multa, ");
		sql.append("cr.juroporcentagem As jurosPorcentagem, cr.juro As juros, cr.valordesconto As desconto, cr.parcela As parcela, ");
		sql.append("tr.nome AS turno, u.nome AS unidadeEnsino, cr.codigo, cr.parceiro, responsavelFinanceiro.nome as responsavelFinanceiro_nome, responsavelFinanceiro.cpf as responsavelFinanceiro_cpf ");
		sql.append(" FROM contaReceber AS cr ");
		sql.append("LEFT JOIN matricula m ON m.matricula = cr.matriculaAluno ");
		sql.append(" left join matriculaperiodo mp on m.matricula = mp.matricula and case when cr.matriculaperiodo is null then mp.codigo = (select codigo from matriculaperiodo mp2 where mp2.matricula = m.matricula order by (mp2.ano||'/'||mp2.semestre )desc limit 1 ) else mo.codigo = cr.matriculaperiodo end");
		sql.append("LEFT JOIN turma t ON t.codigo = mp.turma ");
		sql.append("LEFT JOIN pessoa p ON p.codigo = cr.pessoa ");
		sql.append("LEFT JOIN curso c ON c.codigo = m.curso ");
		sql.append("LEFT JOIN turno tr ON tr.codigo = m.turno ");
		sql.append("INNER JOIN unidadeEnsino u ON cr.unidadeEnsino = u.codigo ");
		sql.append("LEFT JOIN parceiro ON cr.parceiro = parceiro.codigo ");
		sql.append("left join pessoa responsavelFinanceiro on responsavelFinanceiro.codigo = contareceber.responsavelFinanceiro ");
		sql.append("WHERE cr.situacao = 'AR' ");

		if (unidadeEnsinoCursoVO.getUnidadeEnsino() != 0) {
			sql.append("AND cr.unidadeEnsino = ").append(unidadeEnsinoCursoVO.getUnidadeEnsino());

			if (unidadeEnsinoCursoVO.getCurso().getCodigo() != null && unidadeEnsinoCursoVO.getCurso().getCodigo() != 0) {
				sql.append(" AND m.curso = ").append(unidadeEnsinoCursoVO.getCurso().getCodigo());
			}
			if (unidadeEnsinoCursoVO.getTurno().getCodigo() != null && unidadeEnsinoCursoVO.getTurno().getCodigo() != 0) {
				sql.append(" AND m.turno = ").append(unidadeEnsinoCursoVO.getTurno().getCodigo());
			}
			if (turmaVO.getCodigo() != null && turmaVO.getCodigo() != 0) {
				sql.append(" AND mp.turma = ").append(turmaVO.getCodigo());
			}
		} else {
			sql.append(" AND cr.unidadeEnsino <> 0 ");
		}

		if (responsavelFinanceiro != null && responsavelFinanceiro.getCodigo() > 0) {
			sql.append(" and responsavelFinanceiro.codigo = ").append(responsavelFinanceiro.getCodigo());
		}
		if(dataCompetencia != null && dataCompetencia) {
			sql.append(" AND ").append(realizarGeracaoWherePeriodoConsiderandoMesAno(dataInicio, dataFim, "cr.datacompetencia"));
		}else {
			if (dataInicio != null) {
				sql.append(" AND cr.datavencimento between '").append(Uteis.getDataBD0000(dataInicio)).append("' and '").append(Uteis.getDataBD2359(dataFim)).append("' ");
			} else {
				sql.append(" AND cr.datavencimento < '").append(Uteis.getDataBD2359(dataFim)).append("'");
			}
		}
		sql.append(adicionarFiltroRegistroCobranca(situacaoRegristroCobranca, dataInicio, dataFim, dataCompetencia));

		sql.append(" GROUP BY t.identificadorTurma, cr.matriculaaluno, nome1, p.cpf, telresidencial1, p.celular, ");
		sql.append("email1, serasa, c.nome, cr.tipoorigem, cr.dataVencimento, cr.valor, cr.multaporcentagem, cr.multa, ");
		sql.append("cr.juroporcentagem, cr.juro, cr.valordesconto, cr.parcela, tr.nome, u.nome, cr.codigo, cr.parceiro, responsavelFinanceiro.codigo,  responsavelFinanceiro.nome, responsavelFinanceiro.cpf, responsavelFinanceiro.seraza, responsavelFinanceiro.email, responsavelFinanceiro.codigo, responsavelFinanceiro.telefoneres, m.matricula ");
		sql.append(" ORDER BY c.nome, t.identificadorTurma, nome1 ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsulta(tabelaResultado, configuracao);
	}

	public List<InadimplenciaRelVO> montarDadosConsultaRelatorioComDesconto(SqlRowSet tabelaResultado, Boolean trazerValorFinalDescontoCalculado, UsuarioVO usuario) throws Exception {
		List<InadimplenciaRelVO> vetResultado = new ArrayList<InadimplenciaRelVO>(0);
		Map<Integer, ConfiguracaoFinanceiroVO> configuracaoFinanceiroMap = new HashMap<Integer, ConfiguracaoFinanceiroVO>(0);
		while (tabelaResultado.next()) {
			if (!configuracaoFinanceiroMap.containsKey(tabelaResultado.getInt("cr.unidadeEnsino"))) {
				configuracaoFinanceiroMap.put(tabelaResultado.getInt("cr.unidadeEnsino"), getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarPorUnidadeEnsino(tabelaResultado.getInt("cr.unidadeEnsino"), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
			}
			vetResultado.add(montarDadosRelatorioComDesconto(tabelaResultado, trazerValorFinalDescontoCalculado, configuracaoFinanceiroMap.get(tabelaResultado.getInt("cr.unidadeEnsino")), usuario));
		}
		return vetResultado;
	}

	public List<InadimplenciaRelVO> montarDadosConsulta(SqlRowSet tabelaResultado, ConfiguracaoFinanceiroVO configuracao) throws Exception {
		List<InadimplenciaRelVO> vetResultado = new ArrayList<InadimplenciaRelVO>(0);
		List<String> listaMatriculas = new ArrayList<String>(0);
		while (tabelaResultado.next()) {
			InadimplenciaRelVO inadimplenciaRelVO = montarDados(tabelaResultado, configuracao);
			if (!listaMatriculas.contains(inadimplenciaRelVO.getMatricula())) {
				listaMatriculas.add(inadimplenciaRelVO.getMatricula());
				inadimplenciaRelVO.setCont(1);
			} else {
				inadimplenciaRelVO.setCont(0);
			}
			vetResultado.add(inadimplenciaRelVO);
		}
		listaMatriculas = null;
		return vetResultado;
	}

	public InadimplenciaRelVO montarDadosRelatorioComDesconto(SqlRowSet dadosSQL, Boolean trazerValorFinalDescontoCalculado, ConfiguracaoFinanceiroVO configuracao, UsuarioVO usuario) throws Exception {

		InadimplenciaRelVO obj = new InadimplenciaRelVO();
		obj.setIdadeAluno(dadosSQL.getString("idadeAluno"));
		obj.setMatricula(dadosSQL.getString("matricula"));
		obj.setResponsavelFinanceiro(dadosSQL.getString("responsavelFinanceiro_nome"));
		obj.setNome(dadosSQL.getString("nome"));
		obj.setTelresidencial(dadosSQL.getString("telresidencial"));
		obj.setCep(dadosSQL.getString("cep1"));
		obj.setEndereco(dadosSQL.getString("endereco1"));
		obj.setNumero(dadosSQL.getString("numero1"));
		obj.setSetor(dadosSQL.getString("bairro"));
		obj.setComplemento(dadosSQL.getString("complemento1"));
		if (dadosSQL.getString("responsavelFinanceiro_cpf") == null) {
			obj.setCpf(dadosSQL.getString("cpf"));
		} else {
			obj.setCpf(dadosSQL.getString("responsavelFinanceiro_cpf"));
		}
		obj.setSerasa(dadosSQL.getBoolean("serasa"));
		obj.setCidade(dadosSQL.getString("cidade1"));
		obj.setEstado(dadosSQL.getString("estado1"));
		obj.setParceiroNome(dadosSQL.getString("parceiroNome"));
		obj.setParceiroTelComercial1(dadosSQL.getString("parceiroTelComercial1"));
		obj.setParceiroCep(dadosSQL.getString("parceiroCep"));
		obj.setParceiroEndereco(dadosSQL.getString("parceiroEndereco"));
		obj.setParceiroNumero(dadosSQL.getString("parceiroNumero"));
		obj.setParceiroSetor(dadosSQL.getString("parceiroSetor"));
		obj.setParceiroComplemento(dadosSQL.getString("parceiroComplemento"));
		obj.setTurma(dadosSQL.getString("identificadorturma"));
		obj.setUnidadeEnsino(dadosSQL.getString("unidadeEnsino"));
		obj.setUnidadeEnsinoFinanceira(dadosSQL.getString("unidadeEnsinoFinanceira"));
		obj.setCurso(dadosSQL.getString("curso"));
		obj.setTurno(dadosSQL.getString("turno"));
		obj.setAno(dadosSQL.getString("ano"));
		obj.setSemestre(dadosSQL.getString("semestre"));
		obj.setTelcelular(dadosSQL.getString("telcelular"));
		obj.setEmail(dadosSQL.getString("email"));
		obj.setEmail2(dadosSQL.getString("email2"));
		obj.setValorDiaPrimeiroVencimento(dadosSQL.getDouble("valordiaprimeirovencimento"));

		obj.setCodigoContaReceber(dadosSQL.getInt("codigocontareceber"));
		obj.setDataVencimento(dadosSQL.getDate("cr.dataVencimento"));
		obj.setParcela(dadosSQL.getString("cr.parcela"));
		obj.setValor(dadosSQL.getDouble("cr.valor"));
//		obj.setOrigem(TipoOrigemContaReceber.getDescricao(dadosSQL.getString("cr.tipoOrigem")));
		obj.setOrigem(dadosSQL.getString("cr.tipoOrigem"));
		obj.setDesconto(dadosSQL.getDouble("cr.valorDesconto"));
		obj.setNossoNumero(dadosSQL.getString("cr.nossoNumero"));
		obj.setValorDescontoCalculadoPrimeiraFaixaDescontos(dadosSQL.getDouble("cr.valorDescontoCalculadoPrimeiraFaixaDescontos"));
		obj.setAgente(dadosSQL.getString("agente"));
		setJurosPorcentagem(dadosSQL.getDouble("cr.juroPorcentagem"));
		setMultaPorcentagem(dadosSQL.getDouble("cr.multaPorcentagem"));

		// contareceber novo
		obj.getContaReceberVO().setTipoPessoa(dadosSQL.getString("cr.tipoPessoa"));
		obj.getContaReceberVO().setCodigo(dadosSQL.getInt("codigocontareceber"));
		obj.getContaReceberVO().setData(dadosSQL.getDate("cr.data"));
		obj.getContaReceberVO().setSituacao(dadosSQL.getString("cr.situacao"));
		obj.getContaReceberVO().setDataVencimento(dadosSQL.getDate("cr.dataVencimento"));
		obj.getContaReceberVO().setValor(dadosSQL.getDouble("cr.valor"));
		obj.getContaReceberVO().getPessoa().setCodigo(dadosSQL.getInt("cr.pessoa"));
		obj.getContaReceberVO().setNrDocumento(dadosSQL.getString("cr.nrDocumento"));
		obj.getContaReceberVO().setTipoOrigem(dadosSQL.getString("cr.tipoOrigem"));
		obj.getContaReceberVO().getUnidadeEnsino().setCodigo(dadosSQL.getInt("cr.unidadeEnsino"));
		obj.getContaReceberVO().setMatriculaPeriodo(dadosSQL.getInt("cr.matriculaPeriodo"));
		obj.getContaReceberVO().setNovoObj(Boolean.FALSE);
		obj.getContaReceberVO().getMatriculaAluno().setMatricula(dadosSQL.getString("cr.matriculaAluno"));
		obj.getContaReceberVO().getFuncionario().setCodigo(dadosSQL.getInt("cr.funcionario"));
		obj.getContaReceberVO().getCandidato().setCodigo(dadosSQL.getInt("cr.candidato"));
		obj.getContaReceberVO().getConvenio().setCodigo(dadosSQL.getInt("cr.convenio"));
		obj.getContaReceberVO().getParceiroVO().setCodigo(dadosSQL.getInt("cr.parceiro"));
		obj.getContaReceberVO().setValorRecebido(dadosSQL.getDouble("cr.valorRecebido"));
		obj.getContaReceberVO().setJuro(dadosSQL.getDouble("cr.juro"));
		obj.getContaReceberVO().setDescricaoPagamento(dadosSQL.getString("cr.descricaoPagamento"));
		obj.getContaReceberVO().setCodOrigem(dadosSQL.getString("cr.codOrigem"));
		obj.getContaReceberVO().setJuroPorcentagem(dadosSQL.getDouble("cr.juroPorcentagem"));
		obj.getContaReceberVO().setMulta(dadosSQL.getDouble("cr.multa"));
		obj.getContaReceberVO().setAcrescimo(dadosSQL.getDouble("cr.acrescimo"));
		obj.getContaReceberVO().setMultaPorcentagem(dadosSQL.getDouble("cr.multaPorcentagem"));
		obj.getContaReceberVO().setParcela(dadosSQL.getString("cr.parcela"));
		obj.getContaReceberVO().getDescontoProgressivo().setCodigo(dadosSQL.getInt("cr.descontoProgressivo"));
		obj.getContaReceberVO().setValorDescontoRecebido(dadosSQL.getDouble("cr.valorDescontoRecebido"));
		obj.getContaReceberVO().setValorDescontoRateio(dadosSQL.getDouble("cr.valorDescontoRateio"));
		obj.getContaReceberVO().setTipoDesconto(dadosSQL.getString("cr.tipoDesconto"));
		obj.getContaReceberVO().setDataArquivoRemessa(dadosSQL.getDate("cr.dataArquivoRemessa"));
		obj.getContaReceberVO().setDescontoProgressivoUtilizado(FaixaDescontoProgressivo.getEnum(dadosSQL.getString("cr.descontoprogressivoutilizado")));
		obj.getContaReceberVO().setOrdemConvenio(dadosSQL.getInt("cr.ordemConvenio"));
		obj.getContaReceberVO().setOrdemConvenioValorCheio(dadosSQL.getBoolean("cr.OrdemConvenioValorCheio"));
		obj.getContaReceberVO().setOrdemDescontoAluno(dadosSQL.getInt("cr.OrdemDescontoAluno"));
		obj.getContaReceberVO().setOrdemDescontoAlunoValorCheio(dadosSQL.getBoolean("cr.OrdemDescontoAlunoValorCheio"));
		obj.getContaReceberVO().setOrdemDescontoProgressivo(dadosSQL.getInt("cr.OrdemDescontoProgressivo"));
		obj.getContaReceberVO().setOrdemDescontoProgressivoValorCheio(dadosSQL.getBoolean("cr.OrdemDescontoProgressivoValorCheio"));
		obj.getContaReceberVO().setOrdemPlanoDesconto(dadosSQL.getInt("cr.OrdemPlanoDesconto"));
		obj.getContaReceberVO().setOrdemPlanoDescontoValorCheio(dadosSQL.getBoolean("cr.OrdemPlanoDescontoValorCheio"));
		obj.getContaReceberVO().setJustificativaDesconto(dadosSQL.getString("cr.justificativaDesconto"));
		obj.getContaReceberVO().setImpressaoBoletoRealizada(dadosSQL.getBoolean("cr.impressaoBoletoRealizada"));
		obj.getContaReceberVO().setValorDescontoCalculadoPrimeiraFaixaDescontos(dadosSQL.getDouble("cr.valorDescontoCalculadoPrimeiraFaixaDescontos"));
		obj.getContaReceberVO().setValorDescontoCalculadoSegundaFaixaDescontos(dadosSQL.getDouble("cr.valorDescontoCalculadoSegundaFaixaDescontos"));
		obj.getContaReceberVO().setValorDescontoCalculadoTerceiraFaixaDescontos(dadosSQL.getDouble("cr.valorDescontoCalculadoTerceiraFaixaDescontos"));
		obj.getContaReceberVO().setValorDescontoCalculadoQuartaFaixaDescontos(dadosSQL.getDouble("cr.valorDescontoCalculadoQuartaFaixaDescontos"));
		obj.getContaReceberVO().setUpdated(dadosSQL.getTimestamp("cr.updated"));
		
		obj.getContaReceberVO().setUsaDescontoCompostoPlanoDesconto(dadosSQL.getBoolean("cr.usaDescontoCompostoPlanoDesconto"));
		obj.getContaReceberVO().getDescontoProgressivo().setCodigo(new Integer(dadosSQL.getInt("descontoprogressivo.codigo")));
		obj.getContaReceberVO().getDescontoProgressivo().setNome(dadosSQL.getString("descontoprogressivo.nome"));
		obj.getContaReceberVO().getDescontoProgressivo().setDiaLimite1(new Integer(dadosSQL.getInt("descontoprogressivo.diaLimite1")));
		obj.getContaReceberVO().getDescontoProgressivo().setPercDescontoLimite1(new Double(dadosSQL.getDouble("descontoprogressivo.percDescontoLimite1")));
		obj.getContaReceberVO().getDescontoProgressivo().setDiaLimite2(new Integer(dadosSQL.getInt("descontoprogressivo.diaLimite2")));
		obj.getContaReceberVO().getDescontoProgressivo().setPercDescontoLimite2(new Double(dadosSQL.getDouble("descontoprogressivo.percDescontoLimite2")));
		obj.getContaReceberVO().getDescontoProgressivo().setDiaLimite3(new Integer(dadosSQL.getInt("descontoprogressivo.diaLimite3")));
		obj.getContaReceberVO().getDescontoProgressivo().setPercDescontoLimite3(new Double(dadosSQL.getDouble("descontoprogressivo.percDescontoLimite3")));
		obj.getContaReceberVO().getDescontoProgressivo().setDiaLimite4(new Integer(dadosSQL.getInt("descontoprogressivo.diaLimite4")));
		obj.getContaReceberVO().getDescontoProgressivo().setPercDescontoLimite4(new Double(dadosSQL.getDouble("descontoprogressivo.percDescontoLimite4")));
		obj.getContaReceberVO().getDescontoProgressivo().setValorDescontoLimite1(new Double(dadosSQL.getDouble("descontoprogressivo.valorDescontoLimite1")));
		obj.getContaReceberVO().getDescontoProgressivo().setValorDescontoLimite2(new Double(dadosSQL.getDouble("descontoprogressivo.valorDescontoLimite2")));
		obj.getContaReceberVO().getDescontoProgressivo().setValorDescontoLimite3(new Double(dadosSQL.getDouble("descontoprogressivo.valorDescontoLimite3")));
		obj.getContaReceberVO().getDescontoProgressivo().setValorDescontoLimite4(new Double(dadosSQL.getDouble("descontoprogressivo.valorDescontoLimite4")));
		obj.getContaReceberVO().getDescontoProgressivo().setAtivado(dadosSQL.getBoolean("descontoprogressivo.ativado"));
		obj.getContaReceberVO().getDescontoProgressivo().setDataAtivacao(dadosSQL.getDate("descontoprogressivo.dataAtivacao"));
		obj.getContaReceberVO().getDescontoProgressivo().getResponsavelAtivacao().setCodigo(new Integer(dadosSQL.getInt("descontoprogressivo.responsavelAtivacao")));
		obj.getContaReceberVO().getDescontoProgressivo().setDataInativacao(dadosSQL.getDate("descontoprogressivo.dataInativacao"));
		obj.getContaReceberVO().getDescontoProgressivo().getResponsavelInativacao().setCodigo(new Integer(dadosSQL.getInt("descontoprogressivo.responsavelInativacao")));
		obj.getContaReceberVO().getDescontoProgressivo().setUtilizarDiaFixo(dadosSQL.getBoolean("descontoprogressivo.utilizarDiaFixo"));
		obj.getContaReceberVO().getDescontoProgressivo().setUtilizarDiaUtil(dadosSQL.getBoolean("descontoprogressivo.utilizarDiaUtil"));
		
		if (obj.getContaReceberVO().getDescontoProgressivo().getCodigo() > 0){
                    obj.getContaReceberVO().getDescontoProgressivo().setNovoObj(Boolean.FALSE);
		}
		
		// calcularJuroMulta(obj, dadosSQL, configuracao);

		// DADOS PlanoDescontoContaReceberVO
		PlanoDescontoContaReceberVO planoDescontoContaReceberVO = null;

		// HASH PARA TRATAR LISTAS DE UM MESMO OBJETO
		HashMap<Integer, PlanoDescontoContaReceberVO> hashtablePlanoDescontoContaReceber = new HashMap<Integer, PlanoDescontoContaReceberVO>(0);

		do {
			if (!obj.getContaReceberVO().getCodigo().equals(dadosSQL.getInt("codigocontareceber"))) {
				dadosSQL.previous();
				break;
			}
			planoDescontoContaReceberVO = new PlanoDescontoContaReceberVO();

			planoDescontoContaReceberVO.setCodigo(dadosSQL.getInt("pdcr.codigo"));
			planoDescontoContaReceberVO.setContaReceber(dadosSQL.getInt("pdcr.contaReceber"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setCodigo(dadosSQL.getInt("pdcr.planoDesconto"));
			planoDescontoContaReceberVO.setTipoItemPlanoFinanceiro(dadosSQL.getString("pdcr.tipoItemPlanoFinanceiro"));
			planoDescontoContaReceberVO.getConvenio().setCodigo(dadosSQL.getInt("pdcr.convenio"));
			planoDescontoContaReceberVO.setValorUtilizadoRecebimento(dadosSQL.getDouble("pdcr.valorutilizadorecebimento"));
			if(planoDescontoContaReceberVO.getCodigo() > 0){
				planoDescontoContaReceberVO.setNovoObj(Boolean.FALSE);
			}

			planoDescontoContaReceberVO.getPlanoDescontoVO().setCodigo(new Integer(dadosSQL.getInt("pd.codigo")));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setNome(dadosSQL.getString("pd.nome"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setPercDescontoParcela(new Double(dadosSQL.getDouble("pd.percDescontoParcela")));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setPercDescontoMatricula(new Double(dadosSQL.getDouble("pd.percDescontoMatricula")));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setRequisitos(dadosSQL.getString("pd.requisitos"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setDescricao(dadosSQL.getString("pd.descricao"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setSomente1PeriodoLetivoParcela(new Boolean(dadosSQL.getBoolean("pd.somente1PeriodoLetivoParcela")));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setSomente1PeriodoLetivoMatricula(new Boolean(dadosSQL.getBoolean("pd.somente1PeriodoLetivoMatricula")));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setTipoDescontoParcela(dadosSQL.getString("pd.tipoDescontoParcela"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setTipoDescontoMatricula(dadosSQL.getString("pd.tipoDescontoMatricula"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setDiasValidadeVencimento(new Integer(dadosSQL.getInt("pd.diasValidadeVencimento")));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setAtivo(dadosSQL.getBoolean("pd.ativo"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setDataAtivacao(dadosSQL.getDate("pd.dataAtivacao"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setDataInativacao(dadosSQL.getDate("pd.dataInativacao"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().getResponsavelAtivacao().setCodigo(dadosSQL.getInt("pd.responsavelAtivacao"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setDescontoValidoAteDataVencimento(dadosSQL.getBoolean("pd.descontoValidoAteDataVencimento"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setAplicarSobreValorCheio(dadosSQL.getBoolean("pd.aplicarSobreValorCheio"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setUtilizarDiaUtil(dadosSQL.getBoolean("pd.utilizarDiaUtil"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setUtilizarDiaFixo(dadosSQL.getBoolean("pd.utilizarDiaFixo"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setUtilizarAvancoDiaUtil(dadosSQL.getBoolean("pd.utilizarAvancoDiaUtil"));
			
			if(planoDescontoContaReceberVO.getPlanoDescontoVO().getCodigo() > 0){
				planoDescontoContaReceberVO.getPlanoDescontoVO().setNovoObj(Boolean.FALSE);
			}

			planoDescontoContaReceberVO.getPlanoDescontoVO().getResponsavelAtivacao().setCodigo(dadosSQL.getInt("ua.codigo"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().getResponsavelAtivacao().setNome(dadosSQL.getString("ua.nome"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().getResponsavelAtivacao().setUsername(dadosSQL.getString("ua.username"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().getResponsavelAtivacao().setTipoUsuario(dadosSQL.getString("ua.tipoUsuario"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().getResponsavelAtivacao().setDataUltimoAcesso(dadosSQL.getTimestamp("ua.dataUltimoAcesso"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().getResponsavelAtivacao().getParceiro().setCodigo(dadosSQL.getInt("ua.parceiro"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().getResponsavelAtivacao().getPessoa().setCodigo(dadosSQL.getInt("ua.pessoa"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().getResponsavelAtivacao().getPessoa().setNome(dadosSQL.getString("pa.nome"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().getResponsavelAtivacao().setNovoObj(Boolean.FALSE);

			planoDescontoContaReceberVO.getPlanoDescontoVO().getResponsavelInativacao().setCodigo(dadosSQL.getInt("pd.responsavelInativacao"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().getResponsavelInativacao().setCodigo(dadosSQL.getInt("ui.codigo"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().getResponsavelInativacao().setNome(dadosSQL.getString("ui.nome"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().getResponsavelInativacao().setUsername(dadosSQL.getString("ui.username"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().getResponsavelInativacao().setTipoUsuario(dadosSQL.getString("ui.tipoUsuario"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().getResponsavelInativacao().setDataUltimoAcesso(dadosSQL.getDate("ui.dataUltimoAcesso"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().getResponsavelInativacao().getParceiro().setCodigo(dadosSQL.getInt("ui.parceiro"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().getResponsavelInativacao().getPessoa().setCodigo(dadosSQL.getInt("ui.pessoa"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().getResponsavelInativacao().getPessoa().setNome(dadosSQL.getString("pi.nome"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().getResponsavelInativacao().setNovoObj(Boolean.FALSE);

			planoDescontoContaReceberVO.getConvenio().setCodigo(dadosSQL.getInt("c.codigo"));
			planoDescontoContaReceberVO.getConvenio().setDescricao(dadosSQL.getString("c.descricao"));
			planoDescontoContaReceberVO.getConvenio().setDataAssinatura(dadosSQL.getDate("c.dataAssinatura"));
			planoDescontoContaReceberVO.getConvenio().getParceiro().setCodigo(dadosSQL.getInt("c.parceiro"));
			planoDescontoContaReceberVO.getConvenio().setAtivo(dadosSQL.getBoolean("c.ativo"));
			planoDescontoContaReceberVO.getConvenio().setCobertura(dadosSQL.getString("c.cobertura"));
			planoDescontoContaReceberVO.getConvenio().setPreRequisitos(dadosSQL.getString("c.preRequisitos"));
			planoDescontoContaReceberVO.getConvenio().setDataInicioVigencia(dadosSQL.getDate("c.dataInicioVigencia"));
			planoDescontoContaReceberVO.getConvenio().setDataFinalVigencia(dadosSQL.getDate("c.dataFinalVigencia"));
			planoDescontoContaReceberVO.getConvenio().setDescontoMatricula(dadosSQL.getDouble("c.descontoMatricula"));
			planoDescontoContaReceberVO.getConvenio().setTipoDescontoMatricula(dadosSQL.getString("c.tipoDescontoMatricula"));
			planoDescontoContaReceberVO.getConvenio().setDescontoParcela(dadosSQL.getDouble("c.descontoParcela"));
			planoDescontoContaReceberVO.getConvenio().setTipoDescontoParcela(dadosSQL.getString("c.tipoDescontoParcela"));
			planoDescontoContaReceberVO.getConvenio().setBolsaCusteadaParceiroMatricula(dadosSQL.getDouble("c.bolsaCusteadaParceiroMatricula"));
			planoDescontoContaReceberVO.getConvenio().setTipoBolsaCusteadaParceiroMatricula(dadosSQL.getString("c.tipoBolsaCusteadaParceiroMatricula"));
			planoDescontoContaReceberVO.getConvenio().setBolsaCusteadaParceiroParcela(dadosSQL.getDouble("c.bolsaCusteadaParceiroParcela"));
			planoDescontoContaReceberVO.getConvenio().setTipoBolsaCusteadaParceiroParcela(dadosSQL.getString("c.tipoBolsaCusteadaParceiroParcela"));
			planoDescontoContaReceberVO.getConvenio().getFormaRecebimentoParceiro().setCodigo(dadosSQL.getInt("c.formaRecebimentoParceiro"));
			planoDescontoContaReceberVO.getConvenio().setDiaBaseRecebimentoParceiro(dadosSQL.getInt("c.diaBaseRecebimentoParceiro"));
			planoDescontoContaReceberVO.getConvenio().getRequisitante().setCodigo(dadosSQL.getInt("c.requisitante"));
			planoDescontoContaReceberVO.getConvenio().setDataRequisicao(dadosSQL.getDate("c.dataRequisicao"));
			planoDescontoContaReceberVO.getConvenio().getResponsavelAutorizacao().setCodigo(dadosSQL.getInt("c.responsavelAutorizacao"));
			planoDescontoContaReceberVO.getConvenio().setDataAutorizacao(dadosSQL.getDate("c.dataAutorizacao"));
			planoDescontoContaReceberVO.getConvenio().getResponsavelFinalizacao().setCodigo(dadosSQL.getInt("c.responsavelFinalizacao"));
			planoDescontoContaReceberVO.getConvenio().setDataFinalizacao(dadosSQL.getDate("c.dataFinalizacao"));
			planoDescontoContaReceberVO.getConvenio().setSituacao(dadosSQL.getString("c.situacao"));
			planoDescontoContaReceberVO.getConvenio().setValidoParaTodoCurso(dadosSQL.getBoolean("c.validoParaTodoCurso"));
			planoDescontoContaReceberVO.getConvenio().setValidoParaTodaUnidadeEnsino(dadosSQL.getBoolean("c.validoParaTodaUnidadeEnsino"));
			planoDescontoContaReceberVO.getConvenio().setValidoParaTodoTurno(dadosSQL.getBoolean("c.validoParaTodoTurno"));
			planoDescontoContaReceberVO.getConvenio().setPeriodoIndeterminado(dadosSQL.getBoolean("c.periodoIndeterminado"));
			planoDescontoContaReceberVO.getConvenio().setDataAtivacao(dadosSQL.getDate("c.dataAtivacao"));
			planoDescontoContaReceberVO.getConvenio().setDataInativacao(dadosSQL.getDate("c.dataInativacao"));
			planoDescontoContaReceberVO.getConvenio().getResponsavelAtivacao().setCodigo(dadosSQL.getInt("c.responsavelAtivacao"));
			planoDescontoContaReceberVO.getConvenio().getResponsavelInativacao().setCodigo(dadosSQL.getInt("c.responsavelInativacao"));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setCodigo(dadosSQL.getInt("c.descontoProgressivoParceiro"));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setCodigo(dadosSQL.getInt("c.descontoProgressivoAluno"));
			planoDescontoContaReceberVO.getConvenio().setPossuiDescontoAntecipacao(dadosSQL.getBoolean("c.possuiDescontoAntecipacao"));
			planoDescontoContaReceberVO.getConvenio().setCalculadoEmCimaValorLiquido(dadosSQL.getBoolean("c.calculadoEmCimaValorLiquido"));
			planoDescontoContaReceberVO.getConvenio().setAplicarDescontoProgressivoMatricula(dadosSQL.getBoolean("c.aplicarDescontoProgressivoMatricula"));
			planoDescontoContaReceberVO.getConvenio().setAplicarDescontoProgressivoMatriculaParceiro(dadosSQL.getBoolean("c.aplicarDescontoProgressivoMatriculaParceiro"));
			if(planoDescontoContaReceberVO.getConvenio().getCodigo() > 0){
				planoDescontoContaReceberVO.getConvenio().setNovoObj(Boolean.FALSE);
			}

			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setCodigo(new Integer(dadosSQL.getInt("dpa.codigo")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setNome(dadosSQL.getString("dpa.nome"));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setDiaLimite1(new Integer(dadosSQL.getInt("dpa.diaLimite1")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setPercDescontoLimite1(new Double(dadosSQL.getDouble("dpa.percDescontoLimite1")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setDiaLimite2(new Integer(dadosSQL.getInt("dpa.diaLimite2")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setPercDescontoLimite2(new Double(dadosSQL.getDouble("dpa.percDescontoLimite2")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setDiaLimite3(new Integer(dadosSQL.getInt("dpa.diaLimite3")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setPercDescontoLimite3(new Double(dadosSQL.getDouble("dpa.percDescontoLimite3")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setDiaLimite4(new Integer(dadosSQL.getInt("dpa.diaLimite4")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setPercDescontoLimite4(new Double(dadosSQL.getDouble("dpa.percDescontoLimite4")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setValorDescontoLimite1(new Double(dadosSQL.getDouble("dpa.valorDescontoLimite1")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setValorDescontoLimite2(new Double(dadosSQL.getDouble("dpa.valorDescontoLimite2")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setValorDescontoLimite3(new Double(dadosSQL.getDouble("dpa.valorDescontoLimite3")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setValorDescontoLimite4(new Double(dadosSQL.getDouble("dpa.valorDescontoLimite4")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setAtivado(dadosSQL.getBoolean("dpa.ativado"));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setDataAtivacao(dadosSQL.getDate("dpa.dataAtivacao"));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().getResponsavelAtivacao().setCodigo(new Integer(dadosSQL.getInt("dpa.responsavelAtivacao")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setDataInativacao(dadosSQL.getDate("dpa.dataInativacao"));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().getResponsavelInativacao().setCodigo(new Integer(dadosSQL.getInt("dpa.responsavelInativacao")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setUtilizarDiaFixo(dadosSQL.getBoolean("dpa.utilizarDiaFixo"));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setUtilizarDiaUtil(dadosSQL.getBoolean("dpa.utilizarDiaUtil"));
			if(planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().getCodigo() > 0){
				planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setNovoObj(Boolean.FALSE);
			}

			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setCodigo(new Integer(dadosSQL.getInt("dpp.codigo")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setNome(dadosSQL.getString("dpp.nome"));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setDiaLimite1(new Integer(dadosSQL.getInt("dpp.diaLimite1")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setPercDescontoLimite1(new Double(dadosSQL.getDouble("dpp.percDescontoLimite1")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setDiaLimite2(new Integer(dadosSQL.getInt("dpp.diaLimite2")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setPercDescontoLimite2(new Double(dadosSQL.getDouble("dpp.percDescontoLimite2")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setDiaLimite3(new Integer(dadosSQL.getInt("dpp.diaLimite3")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setPercDescontoLimite3(new Double(dadosSQL.getDouble("dpp.percDescontoLimite3")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setDiaLimite4(new Integer(dadosSQL.getInt("dpp.diaLimite4")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setPercDescontoLimite4(new Double(dadosSQL.getDouble("dpp.percDescontoLimite4")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setValorDescontoLimite1(new Double(dadosSQL.getDouble("dpp.valorDescontoLimite1")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setValorDescontoLimite2(new Double(dadosSQL.getDouble("dpp.valorDescontoLimite2")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setValorDescontoLimite3(new Double(dadosSQL.getDouble("dpp.valorDescontoLimite3")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setValorDescontoLimite4(new Double(dadosSQL.getDouble("dpp.valorDescontoLimite4")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setAtivado(dadosSQL.getBoolean("dpp.ativado"));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setDataAtivacao(dadosSQL.getDate("dpp.dataAtivacao"));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().getResponsavelAtivacao().setCodigo(new Integer(dadosSQL.getInt("dpp.responsavelAtivacao")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setDataInativacao(dadosSQL.getDate("dpp.dataInativacao"));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().getResponsavelInativacao().setCodigo(new Integer(dadosSQL.getInt("dpp.responsavelInativacao")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setUtilizarDiaFixo(dadosSQL.getBoolean("dpp.utilizarDiaFixo"));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setUtilizarDiaUtil(dadosSQL.getBoolean("dpp.utilizarDiaUtil"));
			if(planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().getCodigo() > 0){
				planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setNovoObj(Boolean.FALSE);
			}

			if (!hashtablePlanoDescontoContaReceber.containsKey(planoDescontoContaReceberVO.getCodigo()) && planoDescontoContaReceberVO.getCodigo() != 0) {
				obj.getContaReceberVO().getPlanoDescontoContaReceberVOs().add(planoDescontoContaReceberVO);
			}
			hashtablePlanoDescontoContaReceber.put(planoDescontoContaReceberVO.getCodigo(), planoDescontoContaReceberVO);
			if (dadosSQL.isLast()) {
				break;
			}
		} while (dadosSQL.next());
                // Montando descontos que são atualizados todos os dias a noite pela Job
                obj.getContaReceberVO().setValorDesconto(dadosSQL.getDouble("cr.valorDesconto"));
		obj.getContaReceberVO().setValorDescontoLancadoRecebimento(dadosSQL.getDouble("cr.valorDescontoLancadoRecebimento"));
		obj.getContaReceberVO().setValorCalculadoDescontoLancadoRecebimento(dadosSQL.getDouble("cr.valorCalculadoDescontoLancadoRecebimento"));
		obj.getContaReceberVO().setTipoDescontoLancadoRecebimento(dadosSQL.getString("cr.tipoDescontoLancadoRecebimento"));
		obj.getContaReceberVO().setValorDescontoAlunoJaCalculado(dadosSQL.getDouble("cr.valorDescontoAlunoJaCalculado"));
                obj.getContaReceberVO().setValorDescontoInstituicao(dadosSQL.getDouble("cr.descontoInstituicao"));
                obj.getContaReceberVO().setValorDescontoConvenio(dadosSQL.getDouble("cr.descontoConvenio"));
                obj.getContaReceberVO().setValorDescontoProgressivo(dadosSQL.getDouble("cr.valorDescontoProgressivo"));
                obj.getContaReceberVO().setValorDescontoRateio(dadosSQL.getDouble("cr.valorDescontoRateio"));
                // Montando juros e multas que são atualizados todos os dias a noite pela Job            
                obj.setJuros(dadosSQL.getDouble("cr.juro"));
                obj.setMulta(dadosSQL.getDouble("cr.multa"));
                obj.getContaReceberVO().setJuro(dadosSQL.getDouble("cr.valorjurocalculado"));
                obj.getContaReceberVO().setMulta(dadosSQL.getDouble("cr.valormultacalculado"));
                
		if (obj.getContaReceberVO().getSituacaoEQuitada()) {
                    // Se o titulo esta quitado temos que considerar os descontos
                    // calculados,
                    // na data da quitacao, nao chamando o método que monta a lista de
                    // desconto
                    // aplicaveis, considerando a quantidade de dias que falta para o
                    // vencimento.
                    obj.setValor(dadosSQL.getDouble("cr.valorRecebido"));
                    obj.setTotal(dadosSQL.getDouble("cr.valorRecebido"));
		} else  {
                    // Monta a lista de descontos validos para a conta a receber.
                    // Adicionalmente, com base na quantidade
                    // de dias que faltam para o vencimento do titulo a rotina tambem já
                    // define quais descontos devem
                    // ser aplicadas para a conta receber, inicializando os mesmos nos
                    // devidos campos
                    // descontoConvenio, descontoProgressivo, descontoAluno,
                    // descontoInstituicao
                    obj.setTotal(dadosSQL.getDouble("cr.valorrecebercalculado"));
                    obj.getContaReceberVO().setValorRecebido(dadosSQL.getDouble("cr.valorrecebercalculado"));
		}
                obj.getContaReceberVO().setValorDescontoRecebido(obj.getContaReceberVO().getValorTotalDescontoContaReceber());
		obj.setValorFinalDescontosCalculados(obj.getContaReceberVO().getValorTotalDescontoContaReceber());
		obj.setCont(1);
		return obj;
	}

	public InadimplenciaRelVO montarDados(SqlRowSet dadosSQL, ConfiguracaoFinanceiroVO configuracao) throws Exception {
		InadimplenciaRelVO obj = new InadimplenciaRelVO();
		obj.setMatricula(dadosSQL.getString("matricula"));
		obj.setResponsavelFinanceiro(dadosSQL.getString("responsavelFinanceiro_nome"));
		obj.setTurma(dadosSQL.getString("turma"));
		obj.setTurno(dadosSQL.getString("turno"));
		obj.setNome(dadosSQL.getString("nome1"));
		obj.setCpf(dadosSQL.getString("cpf"));
		obj.setEmail(dadosSQL.getString("email1"));
		obj.setTelresidencial(dadosSQL.getString("telresidencial1"));
		obj.setTelcelular(dadosSQL.getString("telcelular"));
		obj.setCurso(dadosSQL.getString("curso"));
		obj.setUnidadeEnsino(dadosSQL.getString("unidadeEnsino"));
		obj.setDataVencimento(dadosSQL.getDate("dataVencimento"));
		obj.setSerasa(dadosSQL.getBoolean("serasa"));
		obj.setValor(dadosSQL.getDouble("valor"));
		obj.setOrigem(TipoOrigemContaReceber.getDescricao(dadosSQL.getString("origem")));
		obj.setDesconto(dadosSQL.getDouble("desconto"));
		obj.setNossoNumero(dadosSQL.getString("nossoNumero"));
		setJurosPorcentagem(dadosSQL.getDouble("jurosPorcentagem"));
		setMultaPorcentagem(dadosSQL.getDouble("multaPorcentagem"));
		obj.setParcela(dadosSQL.getString("parcela"));
		calcularJuroMulta(obj, dadosSQL, configuracao);
		return obj;
	}

	public void calcularJuroMulta(InadimplenciaRelVO obj, SqlRowSet dadosSQL, ConfiguracaoFinanceiroVO configuracao) throws Exception {
		Double valorFinal = 0.0;
		valorFinal = obj.getValor();
		valorFinal = obj.getValor() - (obj.getContaReceberVO().getValorDescontoConvenio() + obj.getContaReceberVO().getValorDescontoInstituicao() + obj.getContaReceberVO().getValorDescontoAlunoJaCalculado());

		Long diasAtraso = 0L;
		diasAtraso = Uteis.nrDiasEntreDatas(new Date(), obj.getDataVencimento());
		if (diasAtraso > 0) {
			if (getMultaPorcentagem() == 0) {
				setMultaPorcentagem(configuracao.getPercentualMultaPadrao());
			}
			double valorComMulta = (valorFinal * (getMultaPorcentagem()) / 100);
			obj.setMulta(valorComMulta);
			if (configuracao.getTipoCalculoJuro().equals("CO")) {
				if (getJurosPorcentagem() == 0) {
					setJurosPorcentagem(configuracao.getPercentualJuroPadrao());
				}
				double atraso = (diasAtraso.doubleValue() / 30);
				double valorComJuro = (valorFinal * Math.pow(((getJurosPorcentagem() / 100) + 1), Uteis.arredondar(atraso, 2, 0))) - valorFinal;
				obj.setJuros(Uteis.arredondar(valorComJuro, 2, 0));
			} else {
				if (getJurosPorcentagem() == 0) {
					setJurosPorcentagem(configuracao.getPercentualJuroPadrao());
				}
				double valorComJuro = (valorFinal * (getJurosPorcentagem() / 100) * 1);
				obj.setJuros(Uteis.arredondar(((valorComJuro / 30) * diasAtraso), 2, 0));
			}
			obj.setTotal(Uteis.arredondar((valorFinal + obj.getJuros() + obj.getMulta()), 2, 0));
		} else {
			obj.setJuros(dadosSQL.getDouble("cr.juro"));
			obj.setMulta(dadosSQL.getDouble("cr.multa"));
			obj.setTotal(Uteis.arredondar((obj.getValor() + obj.getJuros() + obj.getMulta()), 2, 0));
		}
	}

	@Override
	public String caminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro");
	}

	@Override
	public String designIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidade() + ".jrxml");
	}

	@Override
	public String designIReportRelatorioExcel() {
		 return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + "InadimplenciaRel_DetalhadoNovo_Excel" + ".jrxml");
	}
	
	@Override
	public void validarDados(List<UnidadeEnsinoVO> listaUnidadeEnsino, Date dataInicio, Date dataFim, String tipoRelatorio) throws Exception {
		int mesDataFim = 0;
		int mesDataAtual = 0;
		int anoDataFim = 0;
		int anoDataAtual = 0;
		
		if (listaUnidadeEnsino.isEmpty()) {
			throw new Exception(UteisJSF.internacionalizar("msg_InadimplenciaInstituicao_unidadeEnsino"));
		}
		boolean existeUnidade = false;
		for(UnidadeEnsinoVO unidadeEnsino: listaUnidadeEnsino){
			if(unidadeEnsino.getFiltrarUnidadeEnsino()){
				existeUnidade = true;
				break;
			}
		}
		if(!existeUnidade){
			throw new Exception(UteisJSF.internacionalizar("msg_InadimplenciaInstituicao_unidadeEnsino"));
		} 
		if (dataFim != null) {
	        Calendar calendarDataFim = Calendar.getInstance();
	        calendarDataFim.setTime(dataFim);
	        mesDataFim = calendarDataFim.get(Calendar.MONTH) + 1;
	        anoDataFim = calendarDataFim.get(Calendar.YEAR);
	        Calendar calendarDataAtual = Calendar.getInstance();
	        calendarDataAtual.setTime(new Date());
	        mesDataAtual = calendarDataAtual.get(Calendar.MONTH) + 1;
	        anoDataAtual = calendarDataAtual.get(Calendar.YEAR);
		} 
		if (dataFim == null) {
			throw new Exception("A Data Final deve ser informada para a geração do relatório.");
		} else if (dataFim.compareTo(new Date()) > 0) {
			throw new Exception("A Data Final não pode ser maior que a data atual.");
		}
		// if (tipoRelatorio == null || tipoRelatorio.equals("")) {
		// throw new
		// ConsistirException("O Tipo de Relatório deve ser informado para a geração do relatório.");
		// }
	}

	public static String getDesignIReportRelatorioSintetico() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidade() + "Sintetico" + ".jrxml");
	}

	public static String getDesignIReportRelatorioComDesconto() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidade() + ".jrxml");
	}

	public Double getJurosPorcentagem() {
		return jurosPorcentagem;
	}

	public void setJurosPorcentagem(Double jurosPorcentagem) {
		this.jurosPorcentagem = jurosPorcentagem;
	}

	public Double getMultaPorcentagem() {
		return multaPorcentagem;
	}

	public void setMultaPorcentagem(Double multaPorcentagem) {
		this.multaPorcentagem = multaPorcentagem;
	}

	public static void setIdEntidade(String idEntidade) {
		InadimplenciaRel.idEntidade = idEntidade;
	}

	public static String getIdEntidade() {
		return idEntidade;
	}
	
	@Override
	public List<InadimplenciaRelVO> gerarListaInadimplenteEnvioNotificacao(List<CursoVO> cursoVOs,List<UnidadeEnsinoVO> listaUnidadeEnsino, Boolean dataCompetencia,  TurmaVO turmaVO, MatriculaVO matriculaVO, String ordenarPor, Date dataInicio, Date dataFim, UsuarioVO usuario, PessoaVO responsavelFinanceiro, boolean filtrarAlunosSemEmail, FiltroRelatorioAcademicoVO filtroAcademicoVO, Boolean trazerMatriculaSerasa, Boolean consideraUnidadeFinanceira, Boolean imprimirApenasMatriculas, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO,CentroReceitaVO centroReceitaVO) throws Exception {		
		StringBuilder sqlStr = new StringBuilder("");
	    sqlStr.append(" SELECT array_to_string(array_agg('Parcela:' || t.parcela ||'      Vencimento:'|| to_char(t.datavencimento,'dd/mm/yyyy') ||'      Valor: R$ '|| t.valorrecebercalculado ),';' ) AS listacontasreceber, ");
	    sqlStr.append(" t.email,t.email2,t.nome,t.telcelular,t.matriculaaluno,t.unidadeensino,t.codigoUnidadeEnsino,t.pessoa  ");
	    sqlStr.append(" FROM ( ");
	    sqlStr.append("SELECT DISTINCT cr.codigo as contareceber, ");
	    sqlStr.append("CASE WHEN cr.parceiro IS NULL THEN cr.matriculaaluno else cr.matriculaaluno end as matricula,  ");
	    sqlStr.append("CASE WHEN pessoa.nome <> '' then pessoa.nome else p2.nome end as nome, ");
	    sqlStr.append("CASE WHEN cr.parceiro is null then case when responsavelFinanceiro.codigo is null then pessoa.celular else responsavelFinanceiro.celular end else p2.celular end as telcelular, ");
	    sqlStr.append("CASE WHEN cr.parceiro is null then case when responsavelFinanceiro.codigo is null then pessoa.email else responsavelFinanceiro.email end else p2.email end  as email, ");
	    sqlStr.append("case when cr.parceiro is null then case when responsavelFinanceiro.codigo is null then pessoa.codigo else responsavelFinanceiro.codigo end else p2.codigo end  as pessoa,  ");
	    sqlStr.append("CASE WHEN cr.parceiro is null then case when responsavelFinanceiro.codigo is null then pessoa.email2 else responsavelFinanceiro.email2 end else p2.email2 end  as email2,  ");
	    sqlStr.append("unidadeensino.nome as unidadeEnsino , unidadeensino.codigo as codigoUnidadeEnsino ,cr.matriculaaluno,cr.parcela,cr.datavencimento,trim(to_char(cr.valorrecebercalculado, '9G999G990D99')) as valorrecebercalculado,cr.nossonumero ");
		sqlStr.append("FROM contareceber cr ");
		sqlStr.append("INNER JOIN unidadeensino on unidadeensino.codigo = cr.unidadeensino ");
		sqlStr.append("LEFT JOIN descontoprogressivo on descontoprogressivo.codigo = cr.descontoprogressivo ");
		sqlStr.append("LEFT JOIN matricula on (cr.matriculaaluno = matricula.matricula and matricula.naoenviarmensagemcobranca = false) ");
		sqlStr.append("LEFT JOIN matriculaperiodo on matricula.matricula = matriculaperiodo.matricula and case when cr.matriculaperiodo is null then matriculaperiodo.codigo = (select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by (mp.ano||'/'||mp.semestre )desc limit 1 ) else matriculaperiodo.codigo = cr.matriculaperiodo end ");
		sqlStr.append("LEFT JOIN curso on curso.codigo = matricula.curso ");
		sqlStr.append("LEFT JOIN turma t2 on t2.codigo = matriculaperiodo.turma ");
		if (filtroRelatorioFinanceiroVO.getTipoOrigemBolsaCusteadaConvenio() ) {
			sqlStr.append("LEFT JOIN pessoa on pessoa.codigo = cr.pessoa ");
		} else {
			sqlStr.append("INNER JOIN pessoa on pessoa.codigo = cr.pessoa ");
		}
		sqlStr.append("LEFT JOIN pessoa p2 on p2.codigo = matricula.aluno ");
		sqlStr.append("LEFT JOIN parceiro ON cr.parceiro = parceiro.codigo ");
		sqlStr.append("LEFT JOIN PlanoDescontoContaReceber pdcr ON pdcr.contareceber = cr.codigo ");
		sqlStr.append("LEFT JOIN PlanoDesconto pd ON pd.codigo = pdcr.planodesconto ");
		sqlStr.append("LEFT JOIN Usuario ua ON ua.codigo = pd.responsavelativacao ");
		sqlStr.append("LEFT JOIN Pessoa pa ON pa.codigo = ua.pessoa ");
		sqlStr.append("LEFT JOIN Usuario ui ON ui.codigo = pd.responsavelinativacao ");
		sqlStr.append("LEFT JOIN Pessoa pi ON pi.codigo = ui.pessoa ");
		sqlStr.append("LEFT JOIN pessoa responsavelFinanceiro on responsavelFinanceiro.codigo = cr.responsavelFinanceiro ");
		sqlStr.append("WHERE cr.situacao = 'AR' AND cr.valor > 0 ");
		if(Uteis.isAtributoPreenchido(centroReceitaVO.getCodigo())){
			sqlStr.append(" AND cr.centroreceita = ").append(centroReceitaVO.getCodigo().intValue()).append(" ");
		}
		if (!listaUnidadeEnsino.isEmpty()) {
			sqlStr.append(" AND cr.unidadeensino IN (");
			for (UnidadeEnsinoVO ue : listaUnidadeEnsino) {
				if (ue.getFiltrarUnidadeEnsino()) {
					sqlStr.append(ue.getCodigo() + ", ");
				}
			}
			sqlStr.append("0) ");
		}
		sqlStr.append(" AND ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroAcademicoVO, "matriculaperiodo"));
		sqlStr.append(" AND ").append(adicionarFiltroTipoOrigemContaReceber(filtroRelatorioFinanceiroVO, "cr"));
		if (Uteis.isAtributoPreenchido(cursoVOs)) {
			int x = 0;
			for (CursoVO cursoVO : cursoVOs) {
				if(cursoVO.getFiltrarCursoVO()){
				if (x > 0) {
					sqlStr.append(", ");
				}else{
					sqlStr.append(" AND curso.codigo  IN (");
				}
				sqlStr.append(cursoVO.getCodigo());				
				x++;
				}
			}
			if (x > 0) {
				sqlStr.append(" ) ");
			}
		}

		if (turmaVO.getCodigo() != null && turmaVO.getCodigo() != 0) {
			sqlStr.append(" AND matriculaperiodo.codigo > 0 and t2.codigo = ");
			sqlStr.append(turmaVO.getCodigo());
			}
			if (matriculaVO.getMatricula() != null && !matriculaVO.getMatricula().equals("")) {
				sqlStr.append(" AND upper(matricula.matricula) = '").append(matriculaVO.getMatricula().toUpperCase()).append("' ");
			}
			if (!filtroRelatorioFinanceiroVO.getTipoOrigemBolsaCusteadaConvenio()) {
				sqlStr.append(" AND (parceiro.codigo is null) and (pessoa.codigo is not null or responsavelFinanceiro.codigo IS NOT NULL) ");
			}
			if (responsavelFinanceiro != null && responsavelFinanceiro.getCodigo() > 0) {
				sqlStr.append(" AND responsavelFinanceiro.codigo = ").append(responsavelFinanceiro.getCodigo());
			}
			if (dataInicio != null) {
				sqlStr.append(" AND cr.datavencimento between '").append(Uteis.getDataBD0000(dataInicio)).append("' AND '").append(Uteis.getDataBD2359(dataFim)).append("' ");
			} else {
				sqlStr.append(" AND cr.datavencimento < '").append(Uteis.getDataBD2359(dataFim)).append("'");
			}
			if (filtrarAlunosSemEmail) {
				sqlStr.append(" AND (pessoa.email <> '' OR responsavelfinanceiro.email <> '' OR p2.email <> '' OR pessoa.email2 <> '' OR responsavelfinanceiro.email2 <> '' OR p2.email2 <> '') ");
			}

			if (trazerMatriculaSerasa) {
				sqlStr.append(" AND matricula.matriculaserasa = true ");
			}
			if (imprimirApenasMatriculas) {
				sqlStr.append(" AND cr.tipoorigem = 'MAT' ");
			}
			sqlStr.append(" and cr.datavencimento < current_date");
			sqlStr.append(" ORDER BY cr.matriculaaluno,cr.codigo "); 
			sqlStr.append(" ) as t GROUP BY t.email,t.email2,t.nome,t.telcelular,t.matriculaaluno,t.unidadeensino,t.pessoa, t.codigoUnidadeEnsino"); 
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return montarDadosConsultaInadimplenteEnvioNotificacao(tabelaResultado, usuario);	 
	}
	
	public List<InadimplenciaRelVO> montarDadosConsultaInadimplenteEnvioNotificacao(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<InadimplenciaRelVO> vetResultado = new ArrayList<InadimplenciaRelVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDadosInadimplenteEnvioNotificacao(tabelaResultado, usuario));
		}
		return vetResultado;
	}

	public InadimplenciaRelVO montarDadosInadimplenteEnvioNotificacao(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		InadimplenciaRelVO obj = new InadimplenciaRelVO();
		obj.setListaParcelaNotificacaoInadimplente(dadosSQL.getString("listacontasreceber"));
		obj.setEmail(dadosSQL.getString("email"));
		obj.setEmail2(dadosSQL.getString("email2"));
		obj.setNome(dadosSQL.getString("nome"));
		obj.setTelcelular(dadosSQL.getString("telcelular"));
		obj.setMatricula(dadosSQL.getString("matriculaaluno"));
		obj.setUnidadeEnsino(dadosSQL.getString("unidadeensino"));
		obj.getContaReceberVO().getPessoa().setCodigo(dadosSQL.getInt("pessoa"));
		obj.getContaReceberVO().getUnidadeEnsino().setCodigo(dadosSQL.getInt("codigoUnidadeEnsino"));
		return obj;
	}

	
	
}
