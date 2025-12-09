/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package jobs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TagsMensagemAutomaticaEnum;
import negocio.comuns.administrativo.enumeradores.TemplateMensagemAutomaticaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

/**
 * 
 * @author PEDRO
 */
@Service
@Lazy
public class JobNotificacaoSuspensaoMatricula extends SuperFacadeJDBC implements Serializable {

	public void realizarNotificacaoMatriculaSuspensa() {
		try {

			List<UnidadeEnsinoVO> listaUnidade = new ArrayList<UnidadeEnsinoVO>(0);
			listaUnidade.addAll(consultarUnidadeEnsinoConfiguracoes());
			
			for(UnidadeEnsinoVO obj : listaUnidade){
				ConfiguracaoGeralSistemaVO config = getAplicacaoControle().getConfiguracaoGeralSistemaVO(obj.getCodigo(), null);				
				realizarBuscaAlunoAptaParaNotificacao(obj.getCodigo(),getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_1_SUSPENSAO_ALUNO_DOCUMENTACAO_PENDENTE, false,Uteis.NIVELMONTARDADOS_DADOSBASICOS, obj.getCodigo(), null, null), config);
				realizarBuscaAlunoAptaParaNotificacao(obj.getCodigo(),getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_2_SUSPENSAO_ALUNO_DOCUMENTACAO_PENDENTE, false,Uteis.NIVELMONTARDADOS_DADOSBASICOS, obj.getCodigo(), null, null), config);
				realizarBuscaAlunoAptaParaNotificacao(obj.getCodigo(),getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_3_SUSPENSAO_ALUNO_DOCUMENTACAO_PENDENTE, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, obj.getCodigo(), null, null), config);
				realizarBuscaAlunoAptaParaNotificacao(obj.getCodigo(),getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_4_SUSPENSAO_ALUNO_DOCUMENTACAO_PENDENTE, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, obj.getCodigo(), null, null), config);
				realizarBuscaAlunoAptaParaNotificacao(obj.getCodigo(),getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_SUSPENSAO_ALUNO_DOCUMENTACAO_PENDENTE, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, obj.getCodigo(), null, null), config);
			}
			
			
			

		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println(e.getMessage());
			//System.out.println("Erro JobNotificacaoSuspensaoMatricula...");
		}
	}
	
	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoConfiguracoes() {
		List<UnidadeEnsinoVO> listaUnidadeEnsino = new ArrayList<UnidadeEnsinoVO>(0);
		StringBuilder sql = new StringBuilder("");
		sql.append(" select codigo ,configuracoes from unidadeensino");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while (rs.next()) {
			UnidadeEnsinoVO obj = new UnidadeEnsinoVO();
			obj.setCodigo(rs.getInt("codigo"));
			obj.getConfiguracoes().setCodigo(rs.getInt("configuracoes"));
			listaUnidadeEnsino.add(obj);
		}

		return listaUnidadeEnsino;

	}

	public void realizarBuscaAlunoAptaParaNotificacao(Integer codigoUnidadeEnsino,PersonalizacaoMensagemAutomaticaVO mensagemTemplate, ConfiguracaoGeralSistemaVO confEmail) throws Exception {
		StringBuilder sql = new StringBuilder("");

		sql.append(" SELECT  pessoa.codigo, pessoa.nome, pessoa.email, pessoa.email2, matricula.matricula, curso.nome as curso_nome, unidadeensino.nome as unidadeensino_nome,  ");
		sql.append(" ( ARRAY_TO_STRING( ARRAY(SELECT tipodocumento.nome from documetacaomatricula   ");
		sql.append("  inner join tipodocumento on tipodocumento.codigo = documetacaomatricula.tipodedocumento   ");
		sql.append("  where  documetacaomatricula.matricula = matricula.matricula   ");
		sql.append("  and documetacaomatricula.entregue = false), ';' )) AS documentos  ");
		sql.append("  from matricula  		 ");
		sql.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sql.append(" inner join curso on curso.codigo = matricula.curso ");
		sql.append(" inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
		sql.append(" INNER JOIN configuracoes on   configuracoes.codigo = unidadeensino.configuracoes ");
		sql.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.codigo  = (select max(mp.codigo) from matriculaperiodo mp where mp.matricula = matricula.matricula ) ");
		sql.append(" where configuracoes.controlarSuspensaoMatriculaPendenciaDocumentos ");
		sql.append(" and unidadeensino.codigo = ").append(codigoUnidadeEnsino);
		sql.append(" and matricula.situacao = 'AT' and (matriculasuspensa  = false or  matriculasuspensa  is null ) ");
		sql.append(" and matricula.matricula in (select dm.matricula from documetacaomatricula dm where matricula.matricula = dm.matricula ");
		sql.append(" and dm.entregue = false and dm.gerarsuspensaomatricula = true limit 1) ");

		sql.append(" and (select horarioturmadia.data from horarioturmadia ");
		sql.append(" inner join horarioturma on horarioturma.codigo = horarioturmadia.horarioturma ");
		sql.append(" where (case curso.periodicidade when 'SE' then horarioturma.anovigente = matriculaperiodo.ano and horarioturma.semestrevigente = matriculaperiodo.semestre  ");
		sql.append(" when 'AN' then horarioturma.anovigente = matriculaperiodo.ano else true end )  ");
		sql.append(" and horarioturmadia.data >= matriculaperiodo.data ");
		sql.append(" and (horarioturma.turma = matriculaperiodo.turma or horarioturma.turma in (select turmaorigem from turmaagrupada where turmaagrupada.turma = matriculaperiodo.turma)) ");
		sql.append(" order by horarioturmadia.data desc limit 1) >= current_date ");

		sql.append(" and (select horarioturmadia.data from horarioturmadia"); 
		sql.append(" inner join horarioturma on horarioturma.codigo = horarioturmadia.horarioturma ");		
		sql.append(" where (case curso.periodicidade when 'SE' then horarioturma.anovigente = matriculaperiodo.ano and horarioturma.semestrevigente = matriculaperiodo.semestre  ");
		sql.append(" when 'AN' then horarioturma.anovigente = matriculaperiodo.ano else true end )  ");
		sql.append(" and horarioturmadia.data >= matriculaperiodo.data ");
		sql.append(" and (horarioturma.turma = matriculaperiodo.turma or horarioturma.turma in (select turmaorigem from turmaagrupada where turmaagrupada.turma = matriculaperiodo.turma)) ");
		sql.append(" order by horarioturmadia.data limit 1) <= ");
		if (mensagemTemplate.getTemplateMensagemAutomaticaEnum().equals(TemplateMensagemAutomaticaEnum.MENSAGEM_1_SUSPENSAO_ALUNO_DOCUMENTACAO_PENDENTE)) {
			sql.append(" (current_date-nrDiasPrimeiroAvisoRiscoSuspensao )");
			sql.append(" and dataenvionotificacao1  is null and nrDiasPrimeiroAvisoRiscoSuspensao is not null and nrDiasPrimeiroAvisoRiscoSuspensao>0 ");
		} else if (mensagemTemplate.getTemplateMensagemAutomaticaEnum().equals(TemplateMensagemAutomaticaEnum.MENSAGEM_2_SUSPENSAO_ALUNO_DOCUMENTACAO_PENDENTE)) {
			sql.append(" (current_date-nrDiasSegundoAvisoRiscoSuspensao )");
			sql.append(" and dataenvionotificacao1  is not null  ");
			sql.append(" and dataenvionotificacao2  is null  ");
			sql.append(" and nrDiasSegundoAvisoRiscoSuspensao is not null and nrDiasSegundoAvisoRiscoSuspensao>0 ");
		} else if (mensagemTemplate.getTemplateMensagemAutomaticaEnum().equals(TemplateMensagemAutomaticaEnum.MENSAGEM_3_SUSPENSAO_ALUNO_DOCUMENTACAO_PENDENTE)) {
			sql.append(" (current_date-nrDiasTerceiroAvisoRiscoSuspensao )");
			sql.append(" and dataenvionotificacao2  is not null  ");
			sql.append(" and dataenvionotificacao3  is null  ");
			sql.append(" and nrDiasTerceiroAvisoRiscoSuspensao is not null and nrDiasTerceiroAvisoRiscoSuspensao>0 ");
		} else if (mensagemTemplate.getTemplateMensagemAutomaticaEnum().equals(TemplateMensagemAutomaticaEnum.MENSAGEM_SUSPENSAO_ALUNO_DOCUMENTACAO_PENDENTE)) {
			sql.append(" (current_date-nrDiasSuspenderMatriculaPendenciaDocumentos)");
			sql.append(" and dataenvionotificacao3  is not null  ");
			sql.append(" and nrDiasSuspenderMatriculaPendenciaDocumentos is not null and nrDiasSuspenderMatriculaPendenciaDocumentos>0 ");
		} else if (mensagemTemplate.getTemplateMensagemAutomaticaEnum().equals(TemplateMensagemAutomaticaEnum.MENSAGEM_4_SUSPENSAO_ALUNO_DOCUMENTACAO_PENDENTE)) {
			sql.append(" (current_date-nrDiasQuartoAvisoRiscoSuspensao )");
			sql.append(" and dataenvionotificacao3  is not null  ");
			sql.append(" AND ((dataenvionotificacao4  IS NULL ) OR  (current_date -periodicidadeQuartoAvisoRiscoSuspensao = dataenvionotificacao4))");
			sql.append(" and nrDiasTerceiroAvisoRiscoSuspensao is not null and nrDiasQuartoAvisoRiscoSuspensao > 0 ");
		}

		sql.append(" group by pessoa.codigo, pessoa.nome, pessoa.email, pessoa.email2, matricula.matricula, curso.nome, unidadeensino.nome ");
		//sql.append("  limit 10");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		// Percorro o while nesse metodo pois caso aconteca um erro ele continua
		// a enviar email para proximo aluno.
		while (tabelaResultado.next()) {
			realizarPreenchimentoDadosParaEnvioMatriculaSuspensa(tabelaResultado, mensagemTemplate, confEmail);
		}
		sql = null;
	}

	public void realizarPreenchimentoDadosParaEnvioMatriculaSuspensa(SqlRowSet rs, PersonalizacaoMensagemAutomaticaVO mensagemTemplate, ConfiguracaoGeralSistemaVO confEmail) throws Exception {
		try {
			ComunicacaoInternaVO obj = new ComunicacaoInternaVO();
			if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
				obj.setAssunto(mensagemTemplate.getAssunto());
				obj.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
				obj.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
				obj.setMensagem(obterMensagemFormatadaMensagemAlunoDocumentacaoPendente(rs.getString("documentos"), rs.getString("nome"), rs.getString("matricula"), rs.getString("curso_nome"), rs.getString("unidadeEnsino_nome"), mensagemTemplate.getMensagem()));
				PessoaVO pessoa = new PessoaVO();
				pessoa.setCodigo(rs.getInt("codigo"));
				pessoa.setEmail(rs.getString("email"));
				pessoa.setEmail2(rs.getString("email2"));
				realizarEnvioMatriculaSuspensa(rs, obj, pessoa, confEmail);
				realizarAtualizacaoMatricula(rs, mensagemTemplate.getTemplateMensagemAutomaticaEnum());
				if (mensagemTemplate.getTemplateMensagemAutomaticaEnum().equals(TemplateMensagemAutomaticaEnum.MENSAGEM_SUSPENSAO_ALUNO_DOCUMENTACAO_PENDENTE)) {
					//realizarBuscaDestinatarioTurmaParaNotificacao(rs.getString("matricula"), obj, confEmail);
				}
				obj = null;
			}
		} catch (Exception e) {
			//System.out.println(e.getMessage());
			//System.out.println("Erro JobNotificacaoSuspensaoMatricula...");
		}

	}

	public void realizarBuscaDestinatarioTurmaParaNotificacao(String matricula, ComunicacaoInternaVO obj, ConfiguracaoGeralSistemaVO confEmail) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" SELECT pessoa.codigo, pessoa.nome, pessoa.email, pessoa.email2 from pessoa ");
		sql.append(" inner join funcionario on funcionario.pessoa = pessoa.codigo ");
		sql.append(" inner join funcionariogrupodestinatarios on funcionariogrupodestinatarios.funcionario = funcionario.codigo ");
		sql.append(" inner join grupodestinatarios on grupodestinatarios.codigo = funcionariogrupodestinatarios.grupodestinatarios ");
		sql.append(" inner join turma on turma.grupodestinatarios = grupodestinatarios.codigo ");
		sql.append(" where turma.codigo = (");
		sql.append(" SELECT MatriculaPeriodo.turma FROM Matricula  ");
		sql.append(" inner join MatriculaPeriodo on MatriculaPeriodo.matricula = Matricula.matricula and matriculaperiodo.situacaomatriculaperiodo = 'AT' ");
		sql.append(" where Matricula.matricula = '").append(matricula).append("' ");
		sql.append(" ORDER BY MatriculaPeriodo.ano desc, matriculaperiodo.semestre desc limit 1 ");
		sql.append(" ) ");
		sql.append("  ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		// Percorro o while nesse metodo pois caso aconteca um erro ele continua
		// a enviar email para proximo aluno.
		obj.setAssunto("Aviso do aluno que será suspenso.");
		while (tabelaResultado.next()) {
			PessoaVO pessoa = new PessoaVO();
			pessoa.setCodigo(tabelaResultado.getInt("codigo"));
			pessoa.setEmail(tabelaResultado.getString("email"));
			pessoa.setEmail2(tabelaResultado.getString("email2"));
			obj.setTipoDestinatario("FU");
			realizarEnvioMatriculaSuspensa(tabelaResultado, obj, pessoa, confEmail);
		}
		sql = null;
	}

	public void realizarEnvioMatriculaSuspensa(SqlRowSet rs, ComunicacaoInternaVO obj, PessoaVO pessoa, ConfiguracaoGeralSistemaVO confEmail) throws Exception {
		try {
			getFacadeFactory().getComunicacaoInternaFacade().executarNotificacaoSuspensaoMatricula(obj, pessoa, new UsuarioVO(), confEmail);
		} catch (Exception e) {
			//System.out.println(e.getMessage());
			//System.out.println("Erro JobNotificacaoSuspensaoMatricula...");
		}
	}

	public void realizarAtualizacaoMatricula(SqlRowSet rs, TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum) {
		StringBuilder sql = new StringBuilder("");
		sql.append(" UPDATE matricula  set ");
		if (templateMensagemAutomaticaEnum.equals(TemplateMensagemAutomaticaEnum.MENSAGEM_1_SUSPENSAO_ALUNO_DOCUMENTACAO_PENDENTE)) {
			sql.append(" dataenvionotificacao1 = '").append(Uteis.getDataJDBC(new Date())).append("'");
		} else if (templateMensagemAutomaticaEnum.equals(TemplateMensagemAutomaticaEnum.MENSAGEM_2_SUSPENSAO_ALUNO_DOCUMENTACAO_PENDENTE)) {
			sql.append(" dataenvionotificacao2 = '").append(Uteis.getDataJDBC(new Date())).append("'");
		} else if (templateMensagemAutomaticaEnum.equals(TemplateMensagemAutomaticaEnum.MENSAGEM_3_SUSPENSAO_ALUNO_DOCUMENTACAO_PENDENTE)) {
			sql.append(" dataenvionotificacao3 = '").append(Uteis.getDataJDBC(new Date())).append("'");
		} else if (templateMensagemAutomaticaEnum.equals(TemplateMensagemAutomaticaEnum.MENSAGEM_SUSPENSAO_ALUNO_DOCUMENTACAO_PENDENTE)) {
			sql.append(" matriculasuspensa = true, databasesuspensao = current_date ");
		} else if (templateMensagemAutomaticaEnum.equals(TemplateMensagemAutomaticaEnum.MENSAGEM_4_SUSPENSAO_ALUNO_DOCUMENTACAO_PENDENTE)) {
			sql.append(" dataenvionotificacao4 = '").append(Uteis.getDataJDBC(new Date())).append("'");
		
		} else {
		sql.append(" matriculasuspensa = false");
	}
		sql.append(" where matricula = '").append(rs.getString("matricula")).append("'");
		getConexao().getJdbcTemplate().update(sql.toString());
	}

	public String obterMensagemFormatadaMensagemAlunoDocumentacaoPendente(String documentos, String aluno, String matricula, String curso, String unidadeEnsino, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), aluno);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), matricula);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_UNIDADE_ENSINO.name(), unidadeEnsino);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), curso);
		StringBuilder listaDocs = new StringBuilder("<ul>");
		if (documentos.contains(";")) {
			for (String documento : documentos.split(";")) {
				listaDocs.append("<li>");
				listaDocs.append(" <strong>").append(documento).append("</strong>");
				listaDocs.append("</li>");
			}
		} else {
			listaDocs.append("<li>");
			listaDocs.append(" <strong>").append(documentos).append("</strong>");
			listaDocs.append("</li>");
		}
		listaDocs.append("</ul>");
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.LISTA_DOCUMENTOS.name(), listaDocs.toString());
		return mensagemTexto;

	}
 
	// public String getMensagemLayout1(String documentos) {
	// StringBuilder sb = new StringBuilder();
	// sb.append(" <p> Olá #NOMEALUNO, conforme \"Declaração de Documentos Pendentes\" assinada no ato de sua matrícula e com intuito de mantê-lo informado de sua situação acadêmica, encaminhamos abaixo a(s) pendência(s) ainda não regularizada(s): </p> ");
	// sb.append(" <p><strong> Lista de documentos pendentes: </strong> </p>");
	// for (String documento : documentos.split(";")) {
	// sb.append(" <p> ").append(documento).append(" </p>");
	// }
	// sb.append(" <p> Após o término do prazo estabelecido na Declaração de Documentos Pendentes, sua matrícula será suspensa. </p>");
	// sb.append(" <p> Caso tenha regularizado sua situação acadêmica, por favor, desconsidere este comunicado e entre em contato com o Dep. Pedagógico do IPOG (pedagogico@ipog.edu.br).  </p>");
	// return sb.toString();
	// }
	//
	// public String getMensagemLayout2(String documentos) {
	// StringBuilder sb = new StringBuilder();
	// sb.append(" <p> Olá aluno #NOMEALUNO, conforme comunicado anteriormente enviado, permanece pendente em nossa secretaria acadêmica o(s) documento(s) listado(s) abaixo: </p> ");
	// sb.append(" <p><strong> Lista de documentos pendentes: </strong> </p>");
	// for (String documento : documentos.split(";")) {
	// sb.append(" <p> ").append(documento).append(" </p>");
	// }
	// sb.append(" <p> Será necessária a regularização em até 30 (trinta) dias. Permanecendo pendente sua matrícula será suspensa.</p>");
	// sb.append(" <p> Caso tenha regularizado sua situação acadêmica, por favor, desconsidere este comunicado e entre em contato com o Dep. Pedagógico do IPOG (pedagogico@ipog.edu.br).  </p>");
	// return sb.toString();
	// }
	//
	// public String getMensagemLayoutSuspensao(String documentos) {
	// StringBuilder sb = new StringBuilder();
	// sb.append(" <p> Olá aluno #NOMEALUNO, conforme comunicados anteriormente enviados, sua matrícula será suspensa em até 24 horas, devido a NÃO REGULARIZAÇÃO DE SUA SITUAÇÃO ACADÊMICA, permanecendo inadimplente em nossa secretaria acadêmica o(s) documento(s) listado(s) abaixo:</p> ");
	//
	// sb.append(" <p><strong> Lista de documentos pendentes: </strong> </p>");
	// for (String documento : documentos.split(";")) {
	// sb.append(" <p> ").append(documento).append(" </p>");
	// }
	// sb.append(" <p> Caso tenha regularizado sua situação acadêmica, por favor, desconsidere este comunicado e entre em contato com o Dep. Pedagógico do IPOG (pedagogico@ipog.edu.br).  </p>");
	// return sb.toString();
	// }
}
