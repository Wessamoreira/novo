package negocio.facade.jdbc.faturamento.nfe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.faturamento.nfe.IntegracaoGinfesAlunoItemVO;
import negocio.comuns.faturamento.nfe.IntegracaoGinfesAlunoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.faturamento.nfe.IntegracaoGinfesAlunoItemInterfaceFacade;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;

@Repository
@Scope("singleton")
@Lazy
public class IntegracaoGinfesAlunoItem extends ControleAcesso implements IntegracaoGinfesAlunoItemInterfaceFacade {

	private static final long serialVersionUID = -1L;
	protected static String idEntidade = "IntegracaoGinfesAluno";

	public IntegracaoGinfesAlunoItem() {
		super();
	}

	public void validarDados(IntegracaoGinfesAlunoItemVO obj) throws ConsistirException {
		if (obj.getNome().length() > 150) {
			throw new ConsistirException("Nome do aluno " + obj.getNome() + " deve possuir até 150 caracteres!");
		}
		if (obj.getNomeResponsavel().length() > 150) {
			throw new ConsistirException("Nome do responsável " + obj.getNomeResponsavel() + " deve possuir até 150 caracteres!");
		}
		if (obj.getNumeroIdentificacaoResponsavel().length() > 14) {
			throw new ConsistirException("CPF do responsável " + obj.getNumeroIdentificacaoResponsavel() + " deve possuir 14 caracteres!");
		}
		if (obj.getNumero().length() > 15) {
			obj.setNumero(obj.getNumero().substring(0, 15));
		}
		if (obj.getNumeroResponsavel().length() > 15) {
			obj.setNumeroResponsavel(obj.getNumeroResponsavel().substring(0, 15));
		}
		if (obj.getMatricula().length() > 14) {	
			obj.setMatricula(obj.getMatricula().substring (obj.getMatricula().length() - 14));		
			//throw new ConsistirException("Matrícula do aluno " + obj.getMatricula() + " deve possuir até 14 caracteres!");
		}
		if (obj.getEmail().length() > 100) {
			throw new ConsistirException("Email do aluno " + obj.getEmail() + " deve possuir até 100 caracteres!");
		}
		if (!UteisTexto.isValidEmailAddress(obj.getEmail())) {
			throw new ConsistirException("Email do aluno " + obj.getEmail() + " é invalido!");
		}
		if (obj.getEmailResponsavel().length() > 100) {
			throw new ConsistirException("Email do responsável " + obj.getEmailResponsavel() + " deve possuir até 100 caracteres!");
		}
		if (!UteisTexto.isValidEmailAddress(obj.getEmailResponsavel())) {
			throw new ConsistirException("Email do responsável " + obj.getEmailResponsavel() + "  é invalido!");
		}		
		if (obj.getLogradouro().length() > 100) {
			throw new ConsistirException("Logradouro do aluno " + obj.getLogradouro() + " deve possuir até 100 caracteres!");
		}
		if (obj.getLogradouroResponsavel().length() > 100) {
			throw new ConsistirException("Logradouro do responsável " + obj.getLogradouroResponsavel() + " deve possuir até 100 caracteres!");
		}
		if (obj.getBairro().length() > 60) {
			throw new ConsistirException("Bairro do aluno " + obj.getBairro() + " deve possuir até 60 caracteres!");
		}
		if (obj.getBairroResponsavel().length() > 60) {
			throw new ConsistirException("Bairro do responsável " + obj.getBairroResponsavel() + " deve possuir até 60 caracteres!");
		}
		if (obj.getSerie().length() > 40) {
			throw new ConsistirException("Série do aluno " + obj.getSerie() + " deve possuir até 40 caracteres!");
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirAlunos(final IntegracaoGinfesAlunoVO obj, List<IntegracaoGinfesAlunoItemVO> alunos, UsuarioVO usuario) throws Exception {
		for (IntegracaoGinfesAlunoItemVO aluno : alunos) {
			aluno.setIntegracao(obj);
			incluir(aluno, usuario);
		}		
	}

	
	private void incluir(final IntegracaoGinfesAlunoItemVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO IntegracaoGinfesAlunoItem (integracao, numeroIdentificacao, nome, matricula, dataNascimento, dataInicial, dataFinal, possuiResponsavel, ")
				.append("alunoAtivo, email, cep, numero, logradouro, bairro, uf, municipio, numeroIdentificacaoResponsavel, nomeResponsavel, emailResponsavel, ")
				.append("cepResponsavel, numeroResponsavel, logradouroResponsavel, bairroResponsavel, ufResponsavel, municipioResponsavel, ")
				.append("codigoUnidadeEnsino, serie, valorServico, valorDescontoCondicionado, valorDescontoIncondicionado, valorDesconto,")
				.append("dataInicioDesconto, dataFimDesconto, valorCustos, dataInicioCustos, dataFimCustos, dataInicioCurso, cursoAtivo,codigoAluno, complemento, ")
				.append("parcelaContaReceber, nomeCurso, nomeTurno , numeroidentificacaocpf , numeroidentificacaocertidaonascimento )")
				.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,? ,? )");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getIntegracao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getNumeroIdentificacao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getNome(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getMatricula(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDataNascimento(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDataInicial(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDataFinal(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getPossuiResponsavel(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getAlunoAtivo(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getEmail(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getCep(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getNumero(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getLogradouro(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getBairro(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getUf(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getMunicipio(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getNumeroIdentificacaoResponsavel(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getNomeResponsavel(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getEmailResponsavel(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getCepResponsavel(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getNumeroResponsavel(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getLogradouroResponsavel(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getBairroResponsavel(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getUfResponsavel(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getMunicipioResponsavel(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getCodigoUnidadeEnsinoCurso(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getSerie(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getValorServico(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getValorDescontoCondicionado(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getValorDescontoIncondicionado(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getValorDesconto(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDataInicioDesconto(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDataFimDesconto(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getValorCustos(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDataInicioCustos(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDataFimCustos(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDataInicioCurso(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getCursoAtivo(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getCodigoAluno(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getComplemento(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getParcelaContaReceber(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getNomeCurso(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getNomeTurno(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getNumeroIdentificacaoCPF(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getNumeroIdentificacaoCertidaoNascimento(), ++i, sqlInserir);
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirIntegracaoGinfesAlunoItem(IntegracaoGinfesAlunoItemVO obj, boolean verificarAcesso, UsuarioVO usuario) throws ConsistirException {
		validarDados(obj);
		alterar(obj, "IntegracaoGinfesAlunoItem", new AtributoPersistencia()
				.add("codigoUnidadeEnsino", obj.getCodigoUnidadeEnsinoCurso())
				.add("valorServico", obj.getValorServico())
				, new AtributoPersistencia().add("codigo", obj.getCodigo())
				, usuario);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirAlunos(IntegracaoGinfesAlunoVO obj, UsuarioVO usuario) throws Exception {
			String sql = "DELETE FROM IntegracaoGinfesAlunoItem WHERE integracao = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<IntegracaoGinfesAlunoItemVO> consultarPorIntegracao(Integer integracao, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder("select * from IntegracaoGinfesAlunoItem where integracao = ").append(integracao);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Map<String, List<IntegracaoGinfesAlunoItemVO>> gerarAlunos(IntegracaoGinfesAlunoVO iga, String matricula,  FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder(" ");
		sqlStr.append(" with conta as (");
		sqlStr.append(" 	select ");
		sqlStr.append(" 	min(contareceber.codigo) contareceber,");
		sqlStr.append(" 	min(contareceber.datavencimento) datavencimento,");
		sqlStr.append(" 	contareceber.responsavelfinanceiro,");
		sqlStr.append(" 	contareceber.matriculaaluno,");
		sqlStr.append(" 	contareceber.parcela as parcela_contareceber,");
		sqlStr.append(" 	periodoletivo.descricao serie,");
		sqlStr.append(" 	curso.nome nomecurso,");
		sqlStr.append(" 	to_char(curso.datacriacao, 'DD/MM/YYYY') datainiciocurso,");
		sqlStr.append(" 	turno.nome nometurno,");
		sqlStr.append(" 	unidadeensinocurso.codigo codigoUnidadeEnsino,");
		sqlStr.append(" 	coalesce(uecvg.codigocursoginfes, unidadeensinocurso.codigoCursoUnidadeEnsinoGinfes) codigoCursoUnidadeEnsinoGinfes,");
		sqlStr.append(" 	coalesce(uecvg.valormensalidade, unidadeensinocurso.valormensalidade) valorservico");
		sqlStr.append(" 	from contareceber");
		sqlStr.append(" 	inner join matricula on contareceber.matriculaaluno = matricula.matricula");
		sqlStr.append(" 	inner join matriculaperiodo mpatual on mpatual.codigo = contareceber.matriculaperiodo and mpatual.matricula = matricula.matricula");
		sqlStr.append(" 	inner join periodoletivo on periodoletivo.codigo = mpatual.periodoletivomatricula");
		sqlStr.append(" 	inner join matriculaperiodo mpingresso on mpingresso.matricula = matricula.matricula and mpingresso.codigo = ");
		sqlStr.append(" 	       ( select mpi.codigo from matriculaperiodo mpi");
		sqlStr.append(" 	        where mpi.matricula = matricula.matricula and mpi.situacaomatriculaperiodo != 'PC'");
		sqlStr.append(" 			order by (mpi.ano || '/' || mpi.semestre) asc, case when mpi.situacaomatriculaperiodo in ('AT','PR','FI','FO') then 1 else 2 end, mpi.codigo asc");
		sqlStr.append(" 			limit 1)");
		sqlStr.append(" 	inner join curso on curso.codigo = matricula.curso ");
		sqlStr.append(" 	inner join turno on turno.codigo = matricula.turno");
		sqlStr.append(" 	inner join unidadeensinocurso on unidadeensinocurso.curso = curso.codigo  and unidadeensinocurso.turno = turno.codigo and unidadeensinocurso.unidadeensino = matricula.unidadeensino");
		sqlStr.append(" 	left join unidadeensinocursovaloresginfes uecvg on uecvg.unidadeensinocurso = unidadeensinocurso.codigo");
		sqlStr.append(" 	and (uecvg.numeroperiodoletivocompetenciaparcela = periodoletivo.periodoletivo or uecvg.numeroperiodoletivocompetenciaparcela = 0 ) ");
		sqlStr.append(" 	and  ( (uecvg.anoingresso::text = mpingresso.ano ");
		sqlStr.append(" 				and uecvg.semestreingresso::text =  mpingresso.semestre");
		sqlStr.append(" 				and uecvg.anocompetenciaparcela = ").append(iga.getAnoReferencia());
		sqlStr.append(" 				and uecvg.semestrecompetenciaparcela = ").append(Uteis.getSemestrePorMes(iga.getMesReferencia()));
		sqlStr.append(" 				and curso.periodicidade ='SE'");
		sqlStr.append(" 			) ");
		sqlStr.append(" 			or ");
		sqlStr.append(" 		 	( uecvg.anoingresso::text = mpingresso.ano ");
		sqlStr.append(" 				and uecvg.anocompetenciaparcela = ").append(iga.getAnoReferencia());
		sqlStr.append(" 				and curso.periodicidade ='AN'");
		sqlStr.append(" 			)	");
		sqlStr.append(" 		 )	");
		sqlStr.append(" 																																			");
		sqlStr.append(" and uecvg.codigo = (select unidadeensinocursovaloresginfes.codigo from unidadeensinocursovaloresginfes ");
		sqlStr.append(" 							where  unidadeensinocursovaloresginfes.unidadeensinocurso = uecvg.unidadeensinocurso ");
		sqlStr.append(" 							and unidadeensinocursovaloresginfes.anoingresso = uecvg.anoingresso ");
		sqlStr.append(" 							and unidadeensinocursovaloresginfes.semestreingresso = uecvg.semestreingresso ");
		sqlStr.append(" 							and unidadeensinocursovaloresginfes.anocompetenciaparcela = uecvg.anocompetenciaparcela ");
		sqlStr.append(" 							and unidadeensinocursovaloresginfes.semestrecompetenciaparcela = uecvg.semestrecompetenciaparcela ");
		sqlStr.append(" 							and (unidadeensinocursovaloresginfes.numeroperiodoletivocompetenciaparcela = periodoletivo.periodoletivo or unidadeensinocursovaloresginfes.numeroperiodoletivocompetenciaparcela = 0) ");
		sqlStr.append(" 							order by case when unidadeensinocursovaloresginfes.numeroperiodoletivocompetenciaparcela = 0 then 1 else 0 end, unidadeensinocursovaloresginfes.codigo limit 1 ");
		sqlStr.append(" 					) ");
		
		
		sqlStr.append(" 			");
		sqlStr.append(" 	where 1=1 ");
		if(Uteis.isAtributoPreenchido(matricula)) {
			sqlStr.append(" 	and contareceber.matriculaaluno = '").append(matricula).append("' ");	
		}
		if(iga.getSituacaoContaReceber().equalsIgnoreCase(SituacaoContaReceber.A_RECEBER.getValor())) {
			sqlStr.append("     and contareceber.situacao = 'AR' ");
		}else if(iga.getSituacaoContaReceber().equalsIgnoreCase(SituacaoContaReceber.RECEBIDO.getValor())) {
			sqlStr.append("     and contareceber.situacao = 'RE' ");
		}
		if(Uteis.isAtributoPreenchido(iga.getUnidadeEnsino().getCodigo())) {
			sqlStr.append(" 	and contareceber.unidadeensino = ").append(iga.getUnidadeEnsino().getCodigo()).append("");	
		}
		sqlStr.append(" and ").append(adicionarFiltroTipoOrigemContaReceber(filtroRelatorioFinanceiroVO, "contareceber"));
		sqlStr.append(" 		and extract('year' from contareceber.datavencimento) = ").append(iga.getAnoReferencia());
		sqlStr.append(" 		and extract('month' from contareceber.datavencimento) = ").append(iga.getMesReferencia());
		sqlStr.append(" 		group by     ");
		sqlStr.append(" 		contareceber.responsavelfinanceiro, ");
		sqlStr.append(" 		contareceber.datavencimento, ");
		sqlStr.append(" 		contareceber.matriculaaluno, ");
		sqlStr.append(" 		contareceber.parcela, ");
		sqlStr.append(" 		periodoletivo.descricao,");
		sqlStr.append(" 		curso.nome,");
		sqlStr.append(" 	    curso.datacriacao,");
		sqlStr.append(" 		turno.nome,");
		sqlStr.append(" 		unidadeensinocurso.codigo,");
		sqlStr.append(" 	    coalesce(uecvg.codigocursoginfes, unidadeensinocurso.codigoCursoUnidadeEnsinoGinfes),");
		sqlStr.append(" 	    coalesce(uecvg.valormensalidade, unidadeensinocurso.valormensalidade) ");
		sqlStr.append(" )	");
		sqlStr.append(" select");
		sqlStr.append(" 	distinct regexp_replace(a.rg, '[^0-9]*', '', 'g') numeroidentificacao,");
		sqlStr.append(" 	regexp_replace(a.certidaonascimento, '[^0-9]*', '', 'g') numeroidentificacaocertidaonascimento ,");
		sqlStr.append(" 	regexp_replace(a.cpf, '[^0-9]*', '', 'g') numeroidentificacaocpf ,");
		sqlStr.append(" 	unaccent(a.nome) nome,");
		sqlStr.append(" 	a.codigo codigoAluno,");
		sqlStr.append(" 	m.matricula,");
		sqlStr.append(" 	unaccent(a.complemento) complemento,");
		sqlStr.append(" 	to_char(a.datanasc, 'DD/MM/YYYY') datanascimento,");
		sqlStr.append(" 	to_char(m.data, 'DD/MM/YYYY') datainicial,");
		sqlStr.append(" 	case when (conta.responsavelfinanceiro is not null and conta.responsavelfinanceiro > 0) then 1 else 0 end possuiresponsavel,");
		sqlStr.append(" 	(");
		sqlStr.append(" 		select");
		sqlStr.append(" 		case when mp.situacaomatriculaperiodo = 'AT' then 1 else 0 end");
		sqlStr.append(" 		from matriculaperiodo mp");
		sqlStr.append(" 		where mp.matricula = m.matricula and mp.situacaomatriculaperiodo != 'PC'");
		sqlStr.append(" 		order by (mp.ano || '/' || mp.semestre) desc, case when mp.situacaomatriculaperiodo in ('AT','PR','FI','FO') then 1 else 2 end, mp.codigo desc");
		sqlStr.append(" 		limit 1) alunoativo,");
		sqlStr.append(" 	unaccent(a.email) email,");
		sqlStr.append(" 	regexp_replace(a.cep, '[^0-9]*', '', 'g') cep,");
		sqlStr.append(" 	unaccent(a.numero) numero,");
		sqlStr.append(" 	unaccent(a.endereco) logradouro,");
		sqlStr.append(" 	unaccent(a.setor) bairro,");
		sqlStr.append(" 	coalesce(e.codigoibge, '0')::int uf,");
		sqlStr.append(" 	coalesce(substring(c.codigoibge from 3), '0')::int municipio,");
		sqlStr.append(" 	regexp_replace(r.cpf, '[^0-9]*', '', 'g') numeroidentificacaoresponsavel,");
		sqlStr.append(" 	unaccent(r.nome) nomeresponsavel,");
		sqlStr.append(" 	unaccent(r.email) emailresponsavel,");
		sqlStr.append(" 	regexp_replace(r.cep, '[^0-9]*', '', 'g') cepresponsavel,");
		sqlStr.append(" 	unaccent(r.numero) numeroresponsavel,");
		sqlStr.append(" 	unaccent(r.endereco) logradouroresponsavel,");
		sqlStr.append(" 	unaccent(r.setor) bairroresponsavel,");
		sqlStr.append(" 	coalesce(er.codigoibge, '0')::int ufresponsavel,");
		sqlStr.append(" 	coalesce(substring(cre.codigoibge from 3), '0')::int municipioresponsavel,");
		sqlStr.append(" 	conta.codigounidadeensino,");
		sqlStr.append(" 	conta.serie,");
		sqlStr.append(" 	conta.codigoCursoUnidadeEnsinoGinfes,");
		sqlStr.append(" 	conta.valorservico,");
		sqlStr.append(" 	conta.nomecurso,");
		sqlStr.append(" 	conta.datainiciocurso,");
		sqlStr.append(" 	conta.nometurno,");
		sqlStr.append(" 	conta.contareceber,");
		sqlStr.append(" 	conta.parcela_contareceber,");
		sqlStr.append(" 	conta.datavencimento");
		sqlStr.append(" from conta");
		sqlStr.append(" inner join matricula m on m.matricula = conta.matriculaaluno");
		sqlStr.append(" inner join pessoa a on a.codigo = m.aluno");
		sqlStr.append(" left join cidade c on c.codigo = a.cidade");
		sqlStr.append(" left join estado e on e.codigo = c.estado");
		sqlStr.append(" left join pessoa r on r.codigo = coalesce(conta.responsavelfinanceiro, a.codigo)");
		sqlStr.append(" left join cidade cre on cre.codigo = r.cidade");
		sqlStr.append(" left join estado er on er.codigo = cre.estado");
		sqlStr.append(" order by unaccent(a.nome)");
		
		
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<IntegracaoGinfesAlunoItemVO> vetResultado = new ArrayList<>(0);
		List<IntegracaoGinfesAlunoItemVO> vetResultadoComErro = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			IntegracaoGinfesAlunoItemVO obj = new IntegracaoGinfesAlunoItemVO();
			obj.setNovoObj(Boolean.TRUE);
			obj.setNumeroIdentificacao(tabelaResultado.getString("numeroIdentificacao"));
			obj.setNome(tabelaResultado.getString("nome"));
			obj.setMatricula(tabelaResultado.getString("matricula"));
			obj.setDataNascimento(tabelaResultado.getString("dataNascimento"));
			obj.setDataInicial(tabelaResultado.getString("dataInicial"));
			obj.setDataFinal("31/12/" + iga.getAnoReferencia());
			obj.setPossuiResponsavel(tabelaResultado.getInt("possuiResponsavel"));			
			obj.setAlunoAtivo(tabelaResultado.getInt("alunoAtivo"));
			obj.setEmail(tabelaResultado.getString("email"));
			obj.setCep(tabelaResultado.getString("cep"));
			obj.setNumero(tabelaResultado.getString("numero"));
			obj.setLogradouro(tabelaResultado.getString("logradouro"));
			obj.setBairro(tabelaResultado.getString("bairro"));
			obj.setUf(tabelaResultado.getInt("uf"));
			obj.setMunicipio(tabelaResultado.getInt("municipio"));
			obj.setNumeroIdentificacaoResponsavel(tabelaResultado.getString("numeroIdentificacaoResponsavel"));
			obj.setNomeResponsavel(tabelaResultado.getString("nomeResponsavel"));
			obj.setEmailResponsavel(tabelaResultado.getString("emailResponsavel"));
			obj.setCepResponsavel(tabelaResultado.getString("cepResponsavel"));
			obj.setNumeroResponsavel(tabelaResultado.getString("numeroResponsavel"));
			obj.setLogradouroResponsavel(tabelaResultado.getString("logradouroResponsavel"));
			obj.setBairroResponsavel(tabelaResultado.getString("bairroResponsavel"));
			obj.setUfResponsavel(tabelaResultado.getInt("ufResponsavel"));
			obj.setMunicipioResponsavel(tabelaResultado.getInt("municipioResponsavel"));
			if(Uteis.isAtributoPreenchido((tabelaResultado.getInt("codigoCursoUnidadeEnsinoGinfes")))) {
				obj.setCodigoUnidadeEnsinoCurso((tabelaResultado.getInt("codigoCursoUnidadeEnsinoGinfes")));				
			}else {
				obj.setCodigoUnidadeEnsinoCurso((tabelaResultado.getInt("codigoUnidadeEnsino")));
			}	
			obj.setSerie(tabelaResultado.getString("serie"));
			obj.setDataInicioCurso(tabelaResultado.getString("dataInicial")); // alterei para dataInicial
			obj.setNomeCurso(tabelaResultado.getString("nomecurso"));
			obj.setNomeTurno(tabelaResultado.getString("nometurno"));
			obj.setCodigoAluno(tabelaResultado.getInt("codigoAluno"));
			obj.setComplemento(tabelaResultado.getString("complemento"));	
			obj.setNumeroIdentificacaoCPF(tabelaResultado.getString("numeroidentificacaocpf"));
			obj.setNumeroIdentificacaoCertidaoNascimento(tabelaResultado.getString("numeroidentificacaocertidaonascimento"));
			obj.setCodigoContaReceber(tabelaResultado.getInt("contareceber"));
			obj.setParcelaContaReceber(tabelaResultado.getString("parcela_contareceber"));
			obj.setValorDescontoCondicionado(0.0);
			obj.setValorDescontoIncondicionado(0.0);
			ContaReceberVO contaAuxiliar = new ContaReceberVO();
			if(iga.getDescontoCondicional()|| iga.getDescontoIncondicional()|| iga.getTrazerValorServicoContaReceber()) {
				contaAuxiliar = getFacadeFactory().getContaReceberFacade().realizarCalculoValorOriginalContaReceberUtilizandoMetodoEspirita(tabelaResultado.getInt("contareceber"));
			}
			if(iga.getTrazerValorServicoContaReceber()) {
				obj.setValorServico(contaAuxiliar.getValor());	
			}else {
				obj.setValorServico(tabelaResultado.getDouble("valorservico"));
			}		
	        if(iga.getDescontoCondicional() && contaAuxiliar != null && !contaAuxiliar.getListaDescontosAplicavesContaReceber().isEmpty()) {
				obj.setValorDescontoCondicionado(Uteis.arrendondarForcandoCadasDecimais(contaAuxiliar.getListaDescontosAplicavesContaReceber().get(0).getValorDescontoComValidade(), 2));
			}
			if(iga.getDescontoIncondicional()  && contaAuxiliar != null && !contaAuxiliar.getListaDescontosAplicavesContaReceber().isEmpty()) {
				obj.setValorDescontoIncondicionado(Uteis.arrendondarForcandoCadasDecimais(contaAuxiliar.getListaDescontosAplicavesContaReceber().get(0).getValorDescontoSemValidade(),2));
			}
//			Double descontoCondicinal = contaAuxiliar.getValorDescontoProgressivo();
//			Double descontoIncondicinal = contaAuxiliar.getValorDescontoAlunoJaCalculado()+contaAuxiliar.getValorDescontoRateio();
//			for(PlanoDescontoContaReceberVO planoDescontoContaReceberVO: contaAuxiliar.getPlanoDescontoContaReceberVOs()) {
//				
//				if(planoDescontoContaReceberVO.getTipoItemPlanoFinanceiro().equals("CO") 
//						&& !planoDescontoContaReceberVO.getConvenio().getAbaterDescontoNoValorParcela()) {
//					descontoIncondicinal += planoDescontoContaReceberVO.getValorUtilizadoRecebimento();
//				}else if(!planoDescontoContaReceberVO.getTipoItemPlanoFinanceiro().equals("CO") && planoDescontoContaReceberVO.getUtilizarDescontoSemLimiteValidade()) {
//					descontoIncondicinal += planoDescontoContaReceberVO.getValorUtilizadoRecebimento();
//				}else if(!planoDescontoContaReceberVO.getTipoItemPlanoFinanceiro().equals("CO") && !planoDescontoContaReceberVO.getUtilizarDescontoSemLimiteValidade()) {
//					descontoCondicinal += planoDescontoContaReceberVO.getValorUtilizadoRecebimento();					
//				}
//			}
//			obj.setValorDescontoCondicionado(descontoCondicinal);
//			obj.setValorDescontoIncondicionado(descontoIncondicinal);
//			if ((valorMensalidade.doubleValue() - obj.getValorServico().doubleValue()) > 0.01) {
//				obj.setValorCustos(valorMensalidade - obj.getValorServico());
//				Calendar c = Calendar.getInstance();
//				c.setTime(tabelaResultado.getDate("datavencimento"));
//				c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
//				obj.setDataFimCustos(Uteis.getData(c.getTime(), "dd/MM/yyyy"));
//				c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
//				obj.setDataInicioCustos(Uteis.getData(c.getTime(), "dd/MM/yyyy"));
//			} else if ((obj.getValorServico().doubleValue() - valorMensalidade.doubleValue()) > 0.01) {
//				obj.setValorDesconto(obj.getValorServico() - valorMensalidade);
//				Calendar c = Calendar.getInstance();
//				c.setTime(tabelaResultado.getDate("datavencimento"));
//				c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
//				obj.setDataFimDesconto(Uteis.getData(c.getTime(), "dd/MM/yyyy"));
//				c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
//				obj.setDataInicioDesconto(Uteis.getData(c.getTime(), "dd/MM/yyyy"));
//			}
			obj.setCursoAtivo(1);
			adicionarIntegracaoGinfesAlunoItemVO(obj, vetResultado, vetResultadoComErro ,iga.getDescontoCondicional(),iga.getDescontoIncondicional(),iga.getTrazerContasAlunoDescontoTotal());			
		}
//		ConsistirException consistirException =  new ConsistirException();
//		ProcessarParalelismo.executar(0, vetResultado.size(), consistirException, new ProcessarParalelismo.Processo() {			
//			@Override
//			public void run(int i) {
//				vetResultado.get(i);
//				
//			}
//		});
		Map<String, List<IntegracaoGinfesAlunoItemVO>> resultado  = new HashMap<>();
		resultado.put(Uteis.SUCESSO, vetResultado);
		resultado.put(Uteis.ERRO, vetResultadoComErro);
		return resultado;
	}
	
	@Override
	public void realizarAtualizacaoAluno(IntegracaoGinfesAlunoVO integracaoGinfesAlunoVO, IntegracaoGinfesAlunoItemVO integracaoGinfesAlunoItemVO, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, UsuarioVO usuarioVO) throws Exception{
		Map<String, List<IntegracaoGinfesAlunoItemVO>> resultado = gerarAlunos(integracaoGinfesAlunoVO, integracaoGinfesAlunoItemVO.getMatricula(), filtroRelatorioFinanceiroVO, usuarioVO);
		if(integracaoGinfesAlunoItemVO.getPossuiErro()) {
			integracaoGinfesAlunoVO.getAlunosErro().remove(integracaoGinfesAlunoItemVO);
			for(IntegracaoGinfesAlunoItemVO obj :resultado.get(Uteis.ERRO)) {
				if(!obj.getCodigoContaReceber().equals(integracaoGinfesAlunoItemVO.getCodigoContaReceber())) {
					resultado.get(Uteis.ERRO).remove(obj);
				}
			}
			
		    
		}else {
			integracaoGinfesAlunoVO.getAlunos().remove(integracaoGinfesAlunoItemVO);
		}
		integracaoGinfesAlunoVO.getAlunos().addAll(resultado.get(Uteis.SUCESSO));
		Ordenacao.ordenarLista(integracaoGinfesAlunoVO.getAlunos(), "nome");		
		integracaoGinfesAlunoVO.getAlunosErro().addAll(resultado.get(Uteis.ERRO));
		Ordenacao.ordenarLista(integracaoGinfesAlunoVO.getAlunosErro(), "nome");
		
	}
	
	private void adicionarIntegracaoGinfesAlunoItemVO(IntegracaoGinfesAlunoItemVO aluno, List<IntegracaoGinfesAlunoItemVO> alunosSemRestricao, List<IntegracaoGinfesAlunoItemVO> alunosComRestricao, Boolean trazerDescontoCondicional, Boolean trazerDescontoInCondicional, Boolean trazerContasAlunoDescontoTotal) throws ParseException {
		// criar a regra de validacao de acordo com o manual
        if(realizarvalidacaoTrazerAlunosDescontosTotal(aluno , trazerContasAlunoDescontoTotal,trazerDescontoCondicional,trazerDescontoInCondicional)) {
        	if (validarIntegracaoGinfesItemVO(aluno)) {
    			aluno.setErroValidacao("<ul>" + aluno.getErroValidacao() + "</ul>");
    			alunosComRestricao.add(aluno);
    		} else {
    			if (aluno.getMatricula().length() > 14) {
    				aluno.setMatricula(aluno.getMatricula().substring(aluno.getMatricula().length() - 14));
    				// throw new ConsistirException("Matrícula do aluno " + obj.getMatricula() + "
    				// deve possuir até 14 caracteres!");
    			}
    			alunosSemRestricao.add(aluno);
    		}
        }
		
	}
	
	private boolean realizarvalidacaoTrazerAlunosDescontosTotal(IntegracaoGinfesAlunoItemVO aluno ,Boolean trazerContasAlunoDescontoTotal,boolean trazerDescontoCondicional, boolean trazerDescontoInCondicional) {
		Double valorServico = aluno.getValorServico();
		Double valorDescontoCondicionado =   trazerDescontoCondicional ? aluno.getValorDescontoCondicionado() : 0.0;
		Double valorDescontoIncondicionado = trazerDescontoInCondicional ? aluno.getValorDescontoIncondicionado() : 0.0 ;
		Double valorTotalComDesconto = valorServico - (valorDescontoCondicionado + valorDescontoIncondicionado); 
		if(trazerContasAlunoDescontoTotal.equals(Boolean.TRUE)) {
			return Boolean.TRUE;
		}else if(valorTotalComDesconto <= 0.0) {
			return Boolean.FALSE;
			
		}
		return Boolean.TRUE;
		
	}

	private Boolean validarIntegracaoGinfesItemVO(IntegracaoGinfesAlunoItemVO aluno) throws ParseException {	
		
			if(aluno.getCep().trim().length() != 8 || aluno.getCep().equals("")) {		
					aluno.setPossuiErro(true);
					aluno.setErroValidacao("<li>O campo CEP está inválido ("+aluno.getCep()+") o mesmo deve possui 8 digitos.</li>");				   
			}
			if(aluno.getNumero().length() >=  15 || aluno.getNumero().isEmpty()) {			
					aluno.setPossuiErro(true);
					aluno.setErroValidacao(aluno.getErroValidacao()+"<li>O campo Numero do (Endereço) do aluno está inválido ("+aluno.getNumero()+") o mesmo não pode ser vazio é deve possui ate  15 digitos.</li>");
				
			}
			if(aluno.getLogradouro().length() >=  100 || aluno.getLogradouro().isEmpty()) {			
					aluno.setPossuiErro(true);
					aluno.setErroValidacao(aluno.getErroValidacao()+"<li>O campo Logadouro do aluno está inválido ("+aluno.getLogradouro()+") o mesmo deve possuir ate  100 digitos.</li>");
			}
			if(aluno.getBairro().trim().isEmpty() || aluno.getBairro().length() >=  60) {			
				  aluno.setPossuiErro(true);
				  aluno.setErroValidacao(aluno.getErroValidacao()+"<li>O campo Bairro está inválido ("+aluno.getBairro()+") o mesmo deve possui ate  60 digitos.</li>");
			}		
			if( aluno.getUf().equals(0)) {			
				 aluno.setPossuiErro(true);
				 aluno.setErroValidacao(aluno.getErroValidacao()+"<li>O campo UF está inválido ("+aluno.getUf()+") o mesmo deve ser preenchido.</li>");			
			}
			if(aluno.getMunicipio().equals(0)) {		
				 aluno.setPossuiErro(true);
				 aluno.setErroValidacao(aluno.getErroValidacao()+"<li>O campo codigo ibge está inválido ("+aluno.getMunicipio()+")  o mesmo deve ser preenchido.</li>");			
			}
			if(aluno.getComplemento().trim().length() > 60) {			
				 aluno.setPossuiErro(true);
				 aluno.setErroValidacao(aluno.getErroValidacao()+"<li>O campo complemento está inválido ("+aluno.getComplemento()+")  o mesmo deve ser preenchido.</li>");
				
			}
			
			if(aluno.getPossuiResponsavel().equals(1)) {
				    aluno.setPossuiErro(false);
				    aluno.setErroValidacao("");
				if(aluno.getCepResponsavel().trim().length() != 8 ||aluno.getCepResponsavel().equals("")) {
					aluno.setPossuiErro(true);
					aluno.setErroValidacao("<li>O campo CEP responsavel está inválido ("+aluno.getCepResponsavel()+") o mesmo deve possui 8 digitos.</li>");
					
				}	
				if(aluno.getMunicipioResponsavel().equals(0) ) {
					 aluno.setPossuiErro(true);
					 aluno.setErroValidacao(aluno.getErroValidacao()+"<li>O campo codigo ibge responsavel está inválido ("+aluno.getMunicipioResponsavel()+")  o mesmo deve ser preenchido.</li>");
					
				}
				if(aluno.getUfResponsavel().equals(0)) {
					 aluno.setPossuiErro(true);
					 aluno.setErroValidacao(aluno.getErroValidacao()+"<li>O campo UF responsavel está inválido ("+aluno.getUf()+") o mesmo deve ser preenchido.</li>");
					
				}
				
			}
			
			if(aluno.getLogradouroResponsavel().length() >=  100 || aluno.getLogradouroResponsavel().isEmpty()) {
				aluno.setPossuiErro(true);
				aluno.setErroValidacao(aluno.getErroValidacao()+"<li>O campo Logadouro do responsável está inválido ("+aluno.getLogradouroResponsavel()+") o mesmo deve possuir ate  100 digitos.</li>");
				
			}
			
			if(aluno.getBairroResponsavel().trim().isEmpty() || aluno.getBairroResponsavel().length() >=  60) {			
					aluno.setPossuiErro(true);
					aluno.setErroValidacao(aluno.getErroValidacao()+"<li>O campo Bairro do responsável está inválido ("+aluno.getBairroResponsavel()+") o mesmo deve possui ate  60 digitos.</li>");
					
			}
			if(aluno.getNumeroResponsavel().length() >=  15 || aluno.getNumeroResponsavel().isEmpty()) {
				aluno.setPossuiErro(true);
				aluno.setErroValidacao(aluno.getErroValidacao()+"<li>O campo Numero do (Endereço) do responsável está inválido ("+aluno.getNumeroResponsavel()+") o mesmo não pode ser vazio é deve possui ate  15 digitos.</li>");
				
			}	
			if (aluno.getNome().length() > 150) {
				aluno.setPossuiErro(true);
				aluno.setErroValidacao(aluno.getErroValidacao()+"<li>O campo Nome do aluno  está inválido ("+aluno.getNome()+") o mesmo deve possui ate  150 digitos.</li>");
				
			}
			
			if (aluno.getEmail().length() > 100) {
				aluno.setPossuiErro(true);
				aluno.setErroValidacao(aluno.getErroValidacao()+"<li>O Email do aluno está inválido ("+aluno.getEmail()+") deve possuir até 100 caracteres.</li>");
				
			}
			if (!UteisTexto.isValidEmailAddress(aluno.getEmail())) {
				aluno.setPossuiErro(true);
				aluno.setErroValidacao(aluno.getErroValidacao()+"<li>O Email do aluno  ("+aluno.getEmail()+") está inválido.</li>");
				
			}
			if (aluno.getSerie().length() > 40) {
				aluno.setPossuiErro(true);
				aluno.setErroValidacao(aluno.getErroValidacao()+"<li>O campo Série do aluno está inválido ("+aluno.getSerie()+") o mesmo deve possuir até 40 caracteres.</li>");
				
			}
			if((Uteis.calcularIdadePessoa(new Date(), UteisData.getData(aluno.getDataNascimento())) >= 18)) {
		    	if (Uteis.removerMascara(aluno.getNumeroIdentificacaoCPF()).length() > 11 || Uteis.removerMascara(aluno.getNumeroIdentificacaoCPF()).isEmpty()) {
					 aluno.setPossuiErro(true);
				     aluno.setErroValidacao(aluno.getErroValidacao()+"<li> O CPF (Documentos Pessoais ) do aluno está inválido ("+aluno.getNumeroIdentificacaoCPF()+") o mesmo deve possuir ate 11 caracteres!</li>");
				}else if (!Uteis.validaCPF(aluno.getNumeroIdentificacaoCPF())) {
					 aluno.setPossuiErro(true);
				     aluno.setErroValidacao(aluno.getErroValidacao()+"<li> O CPF (Documentos Pessoais ) do aluno não e válido ("+aluno.getNumeroIdentificacaoCPF()+") !</li>");
				}
			  	
			}else if(aluno.getPossuiResponsavel().equals(1)){
				 if(Uteis.removerMascara(aluno.getNumeroIdentificacaoCPF()).length() > 11 || Uteis.removerMascara(aluno.getNumeroIdentificacaoCPF()).isEmpty()) {
					 if(Uteis.removerMascara(aluno.getNumeroIdentificacao()).length() > 14 || Uteis.removerMascara(aluno.getNumeroIdentificacao()).isEmpty()) {
						 if(Uteis.removerMascara(aluno.getNumeroIdentificacaoCertidaoNascimento()).length() > 14 || Uteis.removerMascara(aluno.getNumeroIdentificacaoCertidaoNascimento()).isEmpty()) {
							 aluno.setPossuiErro(true);
							  aluno.setErroValidacao(aluno.getErroValidacao()+"<li> O campo certidao de nascimento  (Documentos Pessoais ) do aluno está inválido ("+aluno.getNumeroIdentificacao()+") o mesmo deve possuir ate 14 caracteres!</li>");
						 } 
					 } 
				 }
			}else {				
				aluno.setPossuiErro(true);
			     aluno.setErroValidacao(aluno.getErroValidacao()+"<li> O aluno  ("+aluno.getNome()+") possui idade menor  que  18  e não possui responsável financeiro !</li>");				
			}
			
			
			if (aluno.getNomeResponsavel().length() > 150) {
				aluno.setPossuiErro(true);
				aluno.setErroValidacao(aluno.getErroValidacao()+"<li>O campo Nome do responsável  está inválido ("+aluno.getNomeResponsavel()+") o mesmo deve possui ate  150 digitos.</li>");
				
			}	
			if (Uteis.removerMascara(aluno.getNumeroIdentificacaoResponsavel()).length() > 14 || Uteis.removerMascara(aluno.getNumeroIdentificacaoResponsavel()).isEmpty() || !Uteis.validaCPF(aluno.getNumeroIdentificacaoResponsavel()) || (Uteis.removerMascara(aluno.getNumeroIdentificacaoResponsavel()).length() == 14 &&  !Uteis.validaCNPJ(aluno.getNumeroIdentificacaoResponsavel()))) {
				aluno.setPossuiErro(true);
				aluno.setErroValidacao(aluno.getErroValidacao()+"<li>O CPF/CNPJ do responsável está inválido ("+aluno.getNumeroIdentificacaoResponsavel()+") o mesmo deve possuir ate 14 caracteres!</li>");
			
			}	
			
			if (aluno.getEmailResponsavel().length() > 100) {
				aluno.setPossuiErro(true);
				aluno.setErroValidacao(aluno.getErroValidacao()+"<li>O Email do responsável está inválido ("+aluno.getEmailResponsavel()+") deve possuir até 100 caracteres.</li>");
			}
			if (!UteisTexto.isValidEmailAddress(aluno.getEmailResponsavel())) {
				aluno.setPossuiErro(true);
				aluno.setErroValidacao(aluno.getErroValidacao()+"<li>O Email do responsável  ("+aluno.getEmailResponsavel()+") está inválido.</li>");
			}
			
	
		if(aluno.getValorServico() < aluno.getValorDescontoCondicionado()) {
			aluno.setPossuiErro(true);
			aluno.setErroValidacao(aluno.getErroValidacao()+"<li>O desconto Condicionado  no valor de  ("+aluno.getValorDescontoCondicionado()+")  não pode ser maior que o valor do serviço .</li>");
		}
		if(aluno.getValorServico() <  aluno.getValorDescontoIncondicionado()) {
			aluno.setPossuiErro(true);
			aluno.setErroValidacao(aluno.getErroValidacao()+"<li>O desconto Incondicionado no valor de  ("+aluno.getValorDescontoIncondicionado()+")  não pode ser maior que o valor do serviço .</li>");
		}		
		
		return aluno.getPossuiErro();
	}
	
	private List<IntegracaoGinfesAlunoItemVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario)  {
		List<IntegracaoGinfesAlunoItemVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			IntegracaoGinfesAlunoItemVO obj = new IntegracaoGinfesAlunoItemVO();
			montarDados(obj, tabelaResultado, nivelMontarDados, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}
	
	private void montarDados(IntegracaoGinfesAlunoItemVO obj, SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario)  {
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.getIntegracao().setCodigo(dadosSQL.getInt("integracao"));
		obj.setNumeroIdentificacao(dadosSQL.getString("numeroIdentificacao"));
		obj.setNome(dadosSQL.getString("nome"));
		obj.setMatricula(dadosSQL.getString("matricula"));
		obj.setDataNascimento(dadosSQL.getString("dataNascimento"));
		obj.setDataInicial(dadosSQL.getString("dataInicial"));
		obj.setDataFinal(dadosSQL.getString("dataFinal"));
		obj.setPossuiResponsavel(dadosSQL.getInt("possuiResponsavel"));
		obj.setAlunoAtivo(dadosSQL.getInt("alunoAtivo"));
		obj.setEmail(dadosSQL.getString("email"));
		obj.setCep(dadosSQL.getString("cep"));
		obj.setNumero(dadosSQL.getString("numero"));
		obj.setLogradouro(dadosSQL.getString("logradouro"));
		obj.setBairro(dadosSQL.getString("bairro"));
		obj.setUf(dadosSQL.getInt("uf"));
		obj.setMunicipio(dadosSQL.getInt("municipio"));
		obj.setNumeroIdentificacaoResponsavel(dadosSQL.getString("numeroIdentificacaoResponsavel"));
		obj.setNomeResponsavel(dadosSQL.getString("nomeResponsavel"));
		obj.setEmailResponsavel(dadosSQL.getString("emailResponsavel"));
		obj.setCepResponsavel(dadosSQL.getString("cepResponsavel"));
		obj.setNumeroResponsavel(dadosSQL.getString("numeroResponsavel"));
		obj.setLogradouroResponsavel(dadosSQL.getString("logradouroResponsavel"));
		obj.setBairroResponsavel(dadosSQL.getString("bairroResponsavel"));
		obj.setUfResponsavel(dadosSQL.getInt("ufResponsavel"));
		obj.setMunicipioResponsavel(dadosSQL.getInt("municipioResponsavel"));
		obj.setCodigoUnidadeEnsinoCurso((dadosSQL.getInt("codigoUnidadeEnsino")));
		obj.setSerie(dadosSQL.getString("serie"));
		obj.setValorServico(dadosSQL.getDouble("valorServico"));
		obj.setValorDescontoCondicionado(dadosSQL.getDouble("valorDescontoCondicionado"));
		obj.setValorDescontoIncondicionado(dadosSQL.getDouble("valorDescontoIncondicionado"));
		obj.setValorDesconto(dadosSQL.getDouble("valorDesconto"));
		obj.setDataInicioDesconto(dadosSQL.getString("dataInicioDesconto"));
		obj.setDataFimDesconto(dadosSQL.getString("dataFimDesconto"));
		obj.setValorCustos(dadosSQL.getDouble("valorCustos"));
		obj.setDataInicioCustos(dadosSQL.getString("dataInicioCustos"));
		obj.setDataFimCustos(dadosSQL.getString("dataFimCustos"));
		obj.setDataInicioCurso(dadosSQL.getString("dataInicioCurso"));
		obj.setCursoAtivo(dadosSQL.getInt("cursoAtivo"));
		obj.setCodigoAluno(dadosSQL.getInt("codigoAluno"));
		obj.setComplemento(dadosSQL.getString("complemento"));
		obj.setParcelaContaReceber(dadosSQL.getString("parcelacontareceber"));
		obj.setNomeCurso(dadosSQL.getString("nomecurso"));
		obj.setNomeTurno(dadosSQL.getString("nometurno"));
		obj.setNumeroIdentificacaoCertidaoNascimento(dadosSQL.getString("numeroidentificacaocertidaonascimento"));
		obj.setNumeroIdentificacaoCPF(dadosSQL.getString("numeroidentificacaocpf"));
		if(obj.getNumeroIdentificacaoCPF().isEmpty() && obj.getPossuiResponsavel().equals(0) ){
			  obj.setNumeroIdentificacaoCPF(dadosSQL.getString("numeroIdentificacaoResponsavel"));
			
		}
		
		
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			return;
		}
	}
	

	public static String getIdEntidade() {
		return IntegracaoGinfesAlunoItem.idEntidade;
	}

	
}
