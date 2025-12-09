package relatorio.negocio.jdbc.processosel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.processosel.enumeradores.SituacaoInscricaoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.processosel.ChamadaCandidatoAprovadoRelVO;
import relatorio.negocio.comuns.processosel.EstatisticaProcessoSeletivoVO;
import relatorio.negocio.comuns.processosel.FiltroRelatorioProcessoSeletivoVO;
import relatorio.negocio.comuns.processosel.enumeradores.TipoRelatorioEstatisticoProcessoSeletivoEnum;
import relatorio.negocio.interfaces.processosel.EstatisticaProcessoSeletivoRelInterfaceFacade;

@Repository
@Lazy
public class EstatisticaProcessoSeletivoRel extends ControleAcesso implements EstatisticaProcessoSeletivoRelInterfaceFacade {

	/**
     *
     */
	private static final long serialVersionUID = -1671460799089230413L;

	@SuppressWarnings("rawtypes")
	@Override
	public List consultarDadosGeracaoEstatistica(TipoRelatorioEstatisticoProcessoSeletivoEnum tipoRelatorio, Integer processoSeletivo, Integer dataProva, Integer sala, Integer unidadeEnsinoCurso, String ano, String semestre, FiltroRelatorioProcessoSeletivoVO filtroRelatorioProcessoSeletivo, String ordenarPor, Integer qtdeDiasNotificarDataProva, Integer chamada, Boolean apresentarNomeCandidatoCaixaAlta, Integer quantidadeCasaDecimalAposVirgula, UsuarioVO usuarioVO, Integer numeroChamada) throws Exception {
		switch (tipoRelatorio) {
		case LISTAGEM_APROVADOS:
			return consultarDadosListaInscricao(tipoRelatorio, processoSeletivo, dataProva, unidadeEnsinoCurso, sala, qtdeDiasNotificarDataProva, ano, semestre, filtroRelatorioProcessoSeletivo, apresentarNomeCandidatoCaixaAlta);
		case LISTAGEM_AUSENTES:
			return consultarDadosListaInscricao(tipoRelatorio, processoSeletivo, dataProva, unidadeEnsinoCurso, sala, qtdeDiasNotificarDataProva, ano, semestre, filtroRelatorioProcessoSeletivo, apresentarNomeCandidatoCaixaAlta);
		case LISTAGEM_MATRICULADOS:
			return consultarDadosListaInscricao(tipoRelatorio, processoSeletivo, dataProva, unidadeEnsinoCurso, sala, qtdeDiasNotificarDataProva, ano, semestre, filtroRelatorioProcessoSeletivo, apresentarNomeCandidatoCaixaAlta);
		case LISTAGEM_NAO_MATRICULADOS:
			return consultarDadosListaInscricao(tipoRelatorio, processoSeletivo, dataProva, unidadeEnsinoCurso, sala, qtdeDiasNotificarDataProva, ano, semestre, filtroRelatorioProcessoSeletivo, apresentarNomeCandidatoCaixaAlta);
		case LISTAGEM_REPROVADOS:
			return consultarDadosListaInscricao(tipoRelatorio, processoSeletivo, dataProva, unidadeEnsinoCurso, sala, qtdeDiasNotificarDataProva, ano, semestre, filtroRelatorioProcessoSeletivo, apresentarNomeCandidatoCaixaAlta);
		case LISTAGEM_FREQUENCIA:
			return consultarDadosListaInscricao(tipoRelatorio, processoSeletivo, dataProva, unidadeEnsinoCurso, sala, qtdeDiasNotificarDataProva, ano, semestre, filtroRelatorioProcessoSeletivo, apresentarNomeCandidatoCaixaAlta);
		case LISTAGEM_PRESENTES:
			return consultarDadosListaInscricao(tipoRelatorio, processoSeletivo, dataProva, unidadeEnsinoCurso, sala, qtdeDiasNotificarDataProva, ano, semestre, filtroRelatorioProcessoSeletivo, apresentarNomeCandidatoCaixaAlta);
		case LISTAGEM_CLASSIFICADOS:
			return consultarDadosListaClassificados(processoSeletivo, dataProva, sala, ordenarPor, unidadeEnsinoCurso, ano, semestre, filtroRelatorioProcessoSeletivo, apresentarNomeCandidatoCaixaAlta, numeroChamada);
		case DADOS_CANDIDATOS:
			return consultarDadosListaInscricao(tipoRelatorio, processoSeletivo, dataProva, unidadeEnsinoCurso, sala, qtdeDiasNotificarDataProva, ano, semestre, filtroRelatorioProcessoSeletivo, apresentarNomeCandidatoCaixaAlta);
		case INSCRITOS_BAIRRO:
			return consultarDadosEstatisticosPorBairro(processoSeletivo, dataProva, sala, unidadeEnsinoCurso, ano, semestre, filtroRelatorioProcessoSeletivo, apresentarNomeCandidatoCaixaAlta);
		case INSCRITOS_CURSO:
			return consultarDadosEstatisticosPorCurso(processoSeletivo, dataProva, sala, unidadeEnsinoCurso, ano, semestre, filtroRelatorioProcessoSeletivo);
		case LISTAGEM_PRESENTE_AUSENTES_CURSO_TURNO_DATA:
			return consultarDadosAusentesPresentesCursoTurnoData(processoSeletivo, dataProva, sala, unidadeEnsinoCurso, ano, semestre, filtroRelatorioProcessoSeletivo);
		case LISTAGEM_MURAL_CANDIDATO:
			return consultarDadosMuralAluno(processoSeletivo, dataProva, sala, unidadeEnsinoCurso, ano, semestre, filtroRelatorioProcessoSeletivo, apresentarNomeCandidatoCaixaAlta);
		case LEMBRETE_DATA_PROVA:
			return consultarDadosListaInscricao(tipoRelatorio, processoSeletivo, dataProva, unidadeEnsinoCurso, sala, qtdeDiasNotificarDataProva, ano, semestre, filtroRelatorioProcessoSeletivo, apresentarNomeCandidatoCaixaAlta);
		case LISTAGEM_CANDIDATOS_CHAMADOS:
			return consultarDadosListaChamados(processoSeletivo, dataProva, sala, ordenarPor, unidadeEnsinoCurso, chamada, ano, semestre, filtroRelatorioProcessoSeletivo, quantidadeCasaDecimalAposVirgula);
		default:
			return consultarDadosListaInscricao(tipoRelatorio, processoSeletivo, dataProva, unidadeEnsinoCurso, sala, qtdeDiasNotificarDataProva, ano, semestre, filtroRelatorioProcessoSeletivo, apresentarNomeCandidatoCaixaAlta);
		}
	}
	
	private List<ChamadaCandidatoAprovadoRelVO> consultarDadosListaClassificados(Integer processoSeletivo, Integer dataProva, Integer sala, String ordenarPor, Integer unidadeEnsinoCurso, String ano, String semestre, FiltroRelatorioProcessoSeletivoVO filtroRelatorioProcessoSeletivo, Boolean apresentarNomeCandidatoCaixaAlta, Integer numeroChamada) throws Exception {
		StringBuilder sql  = new StringBuilder("");
        sql.append("SELECT * FROM ( SELECT COALESCE(inscricao.classificacao, 0) classificacao, ");
		sql.append("  inscricao.codigo AS numeroInscricao, pessoa.codigo AS \"pessoa.codigo\", UPPER(pessoa.nome) AS \"pessoa.nome\", pessoa.email, pessoa.telefoneres, pessoa.celular,"); 
		sql.append("  medianotasprocseletivo, notaredacao, somatorioacertos, curso.nome as  \"curso.nome\" , turno.nome as \"turno.nome\", itemprocessoseletivodataprova as codigoDataProva, "); 
		sql.append("  sala, procseletivocurso.numerovaga, unidadeEnsino.nome as \"unidadeEnsino.nome\", procseletivo.regimeAprovacao, inscricao.chamada ");
		sql.append("  from resultadoprocessoseletivo ");
		sql.append("  inner join inscricao on inscricao.codigo = resultadoprocessoseletivo.inscricao "); 
		sql.append("  inner join unidadeensinocurso on case when resultadoprimeiraopcao = 'AP' then  inscricao.cursoopcao1 else "); 
		sql.append("  case when resultadosegundaopcao = 'AP' then inscricao.cursoopcao2 else inscricao.cursoopcao3 end end  = unidadeensinocurso.codigo ");
		if(unidadeEnsinoCurso != null && unidadeEnsinoCurso > 0){
			sql.append(" and unidadeensinocurso.codigo = ").append(unidadeEnsinoCurso);
		}		
		sql.append("  inner join unidadeEnsino on unidadeEnsino.codigo = unidadeensinocurso.unidadeEnsino ");
		sql.append("  inner join procseletivounidadeensino on procseletivounidadeensino.procseletivo = inscricao.procseletivo ");
		sql.append("  and unidadeEnsino.codigo = procseletivounidadeensino.unidadeEnsino ");
		sql.append("  inner join procseletivocurso on procseletivocurso.unidadeensinocurso = unidadeensinocurso.codigo ");
		sql.append("  and procseletivocurso.procseletivounidadeensino = procseletivounidadeensino.codigo ");
		sql.append("  inner join curso on curso.codigo = unidadeensinocurso.curso ");
		sql.append("  inner join turno on turno.codigo = unidadeensinocurso.turno ");
		sql.append("  inner join pessoa on pessoa.codigo = inscricao.candidato  ");
		sql.append("  inner join procseletivo on procseletivo.codigo = inscricao.procseletivo "); 
		sql.append("  where 1=1 ");
		sql.append("  ").append(adicionarFiltroRelatorioProcessoSeletivo(filtroRelatorioProcessoSeletivo, "inscricao"));		
		if (processoSeletivo > 0) {
			sql.append("  and inscricao.procseletivo = ").append(processoSeletivo);			
		}		
		sql.append("  and (resultadoprimeiraopcao = 'AP' or resultadosegundaopcao = 'AP' or resultadoterceiraopcao = 'AP') ");
		
		if (!ano.equals("")) {
			sql.append(" and procseletivo.ano = '").append(ano).append("'");
		}
		if (!semestre.equals("")) {
			sql.append(" and procseletivo.semestre = '").append(semestre).append("'");
		}		
		sql.append(" ) as t ");
		if (Uteis.isAtributoPreenchido(numeroChamada)) {
			sql.append(" where t.chamada = ").append(numeroChamada);
		}else{
			sql.append(" where classificacao <= numerovaga ");			
		}
		if(dataProva != null && dataProva > 0){
			sql.append(" and codigoDataProva = ").append(dataProva);
		}
		if (dataProva != null && dataProva > 0 && sala != null && sala > 0) {
			sql.append(" and  sala =  ").append(sala);
		}
		if (dataProva != null && dataProva > 0 && sala != null && sala < 0) {			
			sql.append(" and  sala is null  ");
		}
		if(ordenarPor != null && ordenarPor.equals("candidato")){
			sql.append(" order by \"unidadeEnsino.nome\", \"curso.nome\", \"turno.nome\",  \"pessoa.nome\", classificacao ");
		}else{
			sql.append(" order by \"unidadeEnsino.nome\", \"curso.nome\", \"turno.nome\",  classificacao  ");
		}
		List<ChamadaCandidatoAprovadoRelVO> chamadaCandidatoAprovadoRelVOs = new ArrayList<ChamadaCandidatoAprovadoRelVO>(0);
		ChamadaCandidatoAprovadoRelVO obj = null;
		SqlRowSet rs= getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while(rs.next()){
			obj = new ChamadaCandidatoAprovadoRelVO();
			obj.setUnidadeEnsino(rs.getString("unidadeEnsino.nome").toUpperCase());			
			obj.setCurso(rs.getString("curso.nome").toUpperCase());
			obj.setChamada(rs.getInt("chamada"));
			obj.setTurno(rs.getString("turno.nome").toUpperCase());
			if (apresentarNomeCandidatoCaixaAlta) {
				obj.setNomeCandidato(rs.getString("pessoa.nome").toUpperCase());
			} else {
				obj.setNomeCandidato(rs.getString("pessoa.nome"));
			}
			obj.setEmail(rs.getString("email"));
			obj.setTelefoneRes(rs.getString("telefoneRes"));
			obj.setCelular(rs.getString("celular"));
			obj.setClassificacao(rs.getInt("classificacao"));
			obj.setNotaRedacao(rs.getBigDecimal("notaredacao"));
			obj.setMediaNotasProcSeletivo(rs.getBigDecimal("medianotasprocseletivo"));
			obj.setNumeroAcertos(new Double(rs.getDouble("somatorioacertos")).intValue());
			obj.setNumeroInscricao(rs.getInt("numeroInscricao"));
			obj.setNumeroVaga(rs.getInt("numerovaga"));
			obj.setRegimeAprovacao(rs.getString("regimeAprovacao"));
			chamadaCandidatoAprovadoRelVOs.add(obj);
		}
		return chamadaCandidatoAprovadoRelVOs;
    }

	public Integer verificarClassificacaoCandidado(InscricaoVO inscricao) throws Exception {
		StringBuilder sql  = new StringBuilder(" SELECT inscricao.classificacao FROM inscricao WHERE inscricao.codigo = ? ");
		Integer classificacao = 0;
		SqlRowSet rs= getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), inscricao.getCodigo());
		if (rs.next()){
			classificacao = rs.getInt("classificacao");
		}
		return classificacao;
	}
	
	private List<ChamadaCandidatoAprovadoRelVO> consultarDadosListaChamados(Integer processoSeletivo, Integer dataProva, Integer sala, String ordenarPor, Integer unidadeEnsinoCurso, Integer chamada, String ano, String semestre, FiltroRelatorioProcessoSeletivoVO filtroRelatorioProcessoSeletivo, Integer quantidadeCasaDecimalAposVirgula) throws Exception {
		StringBuilder sql  = new StringBuilder("");
		sql.append("SELECT * FROM ( SELECT COALESCE(inscricao.classificacao, 0) classificacao, ");
		sql.append(" inscricao.codigo AS numeroInscricao, inscricao.chamada as chamada, inscricao.candidatoconvocadomatricula, ");
		sql.append(" pessoa.codigo AS \"pessoa.codigo\", UPPER(pessoa.nome) AS \"pessoa.nome\", pessoa.email, pessoa.telefoneres, pessoa.celular, "); 
		sql.append(" medianotasprocseletivo, notaredacao, somatorioacertos, curso.nome as  \"curso.nome\" , turno.nome as \"turno.nome\", itemprocessoseletivodataprova as codigoDataProva, "); 
		sql.append(" sala, procseletivocurso.numerovaga, unidadeEnsino.nome as \"unidadeEnsino.nome\", procseletivo.regimeAprovacao, ");
//		sql.append(" (select rps.inscricao from resultadoprocessoseletivo rps where resultadoprimeiraopcao = 'AP' and rps.inscricao = inscricao.codigo)::Boolean and ");
		sql.append(" (select matricula.inscricao from matricula where matricula.inscricao = inscricao.codigo limit 1) is not null as matriculado ");
		sql.append(" from resultadoprocessoseletivo ");
		sql.append(" inner join inscricao on inscricao.codigo = resultadoprocessoseletivo.inscricao "); 
		sql.append(" inner join unidadeensinocurso on case when resultadoprimeiraopcao = 'AP' then  inscricao.cursoopcao1 else "); 
		sql.append(" case when resultadosegundaopcao = 'AP' then inscricao.cursoopcao2 else inscricao.cursoopcao3 end end  = unidadeensinocurso.codigo ");
		if(unidadeEnsinoCurso != null && unidadeEnsinoCurso > 0){
			sql.append(" and unidadeensinocurso.codigo = ").append(unidadeEnsinoCurso);
		}		
		sql.append("  inner join unidadeEnsino on unidadeEnsino.codigo = unidadeensinocurso.unidadeEnsino ");
		sql.append("  inner join procseletivounidadeensino on procseletivounidadeensino.procseletivo = inscricao.procseletivo ");
		sql.append("  and unidadeEnsino.codigo = procseletivounidadeensino.unidadeEnsino ");
		sql.append("  inner join procseletivocurso on procseletivocurso.unidadeensinocurso = unidadeensinocurso.codigo ");
		sql.append("  and procseletivocurso.procseletivounidadeensino = procseletivounidadeensino.codigo ");
		sql.append("  inner join curso on curso.codigo = unidadeensinocurso.curso ");
		sql.append("  inner join turno on turno.codigo = unidadeensinocurso.turno ");
		sql.append("  inner join pessoa on pessoa.codigo = inscricao.candidato  ");
		sql.append("  inner join procseletivo on procseletivo.codigo = inscricao.procseletivo "); 		
		sql.append("  where 1=1 ");
		sql.append("  ").append(adicionarFiltroRelatorioProcessoSeletivo(filtroRelatorioProcessoSeletivo, "inscricao"));			
		if (processoSeletivo > 0) {
			sql.append("  and inscricao.procseletivo = ").append(processoSeletivo);			
		}		
		sql.append("  and (resultadoprimeiraopcao = 'AP' or resultadosegundaopcao = 'AP' or resultadoterceiraopcao = 'AP') ");
		if (!ano.equals("")) {
			sql.append(" and procseletivo.ano = '").append(ano).append("'");
		}
		if (!semestre.equals("")) {
			sql.append(" and procseletivo.semestre = '").append(semestre).append("'");
		}		
		
		sql.append(" ) as t ");
		sql.append(" where candidatoconvocadomatricula = true ");
		if(dataProva != null && dataProva > 0){
			sql.append(" and codigoDataProva = ").append(dataProva);
		}
		if (dataProva != null && dataProva > 0 && sala != null && sala > 0) {
			sql.append(" and  sala =  ").append(sala);
		}
		if (dataProva != null && dataProva > 0 && sala != null && sala < 0) {			
			sql.append(" and  sala is null  ");
		}
		if (!chamada.equals(0)) {
			sql.append(" and chamada = ").append(chamada);
		}
		if(ordenarPor != null && ordenarPor.equals("candidato")){
			sql.append(" order by \"unidadeEnsino.nome\", \"curso.nome\", \"turno.nome\",  \"pessoa.nome\", classificacao ");
		}else if (ordenarPor.equals("chamada")) {
			sql.append(" order by chamada ");
		} else {
			sql.append(" order by \"unidadeEnsino.nome\", \"curso.nome\", \"turno.nome\",  classificacao  ");
		}
		List<ChamadaCandidatoAprovadoRelVO> chamadaCandidatoAprovadoRelVOs = new ArrayList<ChamadaCandidatoAprovadoRelVO>(0);
		ChamadaCandidatoAprovadoRelVO obj = null;
		SqlRowSet rs= getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while(rs.next()){
			obj = new ChamadaCandidatoAprovadoRelVO();
			obj.setCurso(rs.getString("curso.nome").toUpperCase());
			obj.setUnidadeEnsino(rs.getString("unidadeEnsino.nome").toUpperCase());			
			obj.setTurno(rs.getString("turno.nome").toUpperCase());
			obj.setNomeCandidato(rs.getString("pessoa.nome").toUpperCase());
			obj.setClassificacao(rs.getInt("classificacao"));
			
			obj.setNumeroAcertos(new Double(rs.getDouble("somatorioacertos")).intValue());
			obj.setNumeroInscricao(rs.getInt("numeroInscricao"));
			obj.setNumeroVaga(rs.getInt("numerovaga"));
			obj.setRegimeAprovacao(rs.getString("regimeAprovacao"));
			obj.setChamada(rs.getInt("chamada"));
			obj.setMatriculado(rs.getBoolean("matriculado"));
			
			if (obj.getRegimeAprovacao().equals("notaPorDisciplina") || obj.getRegimeAprovacao().equals("quantidadeAcertosRedacao")) {
				
				obj.setNotaRedacao(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(rs.getBigDecimal("notaredacao"), quantidadeCasaDecimalAposVirgula));
				obj.setMediaNotasProcSeletivo(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(rs.getBigDecimal("medianotasprocseletivo"), quantidadeCasaDecimalAposVirgula));
				
			} else {
				obj.setNotaRedacao(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(rs.getBigDecimal("notaredacao"), 2));
				obj.setMediaNotasProcSeletivo(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(rs.getBigDecimal("medianotasprocseletivo"), 2));
			}
			chamadaCandidatoAprovadoRelVOs.add(obj);
		}
		return chamadaCandidatoAprovadoRelVOs;
	}

	private List<InscricaoVO> consultarDadosListaInscricao(TipoRelatorioEstatisticoProcessoSeletivoEnum tipoRelatorio, Integer processoSeletivo, Integer dataProvra, Integer unidadeEnsinoCurso, Integer sala, Integer qtdeDiasNotificarDataProva, String ano, String semestre, FiltroRelatorioProcessoSeletivoVO filtroRelatorioProcessoSeletivo, Boolean apresentarNomeCandidatoCaixaAlta) throws Exception {
		List<InscricaoVO> inscricaoVOs = new ArrayList<InscricaoVO>(0);
		StringBuilder sb = new StringBuilder("SELECT inscricao.codigo, candidato.codigo as candidato_codigo, candidato.nome as candidato_nome, candidato.email as candidato_email, ");
		sb.append(" candidato.rg as candidato_rg, candidato.cpf as candidato_cpf, candidato.telefoneComer as candidato_telefoneComer, candidato.telefoneRes as candidato_telefoneRes, ");
		sb.append(" candidato.telefoneRecado as candidato_telefoneRecado, candidato.celular as candidato_celular, candidato.sexo as candidato_sexo, ");
		sb.append(" procseletivo.codigo as procseletivo_codigo, procseletivo.descricao as procseletivo_descricao, ItemProcSeletivoDataProva.dataProva as ItemProcSeletivoDataProva_dataProva, ");
		sb.append(" cursoopcao1.codigo as cursoopcao1_codigo, cursoopcao1.nome as cursoopcao1_nome, ");
		sb.append(" turnocursoopcao1.codigo as turnocursoopcao1_codigo, turnocursoopcao1.nome as turnocursoopcao1_nome, ");
		sb.append(" cursoopcao2.codigo as cursoopcao2_codigo, cursoopcao2.nome as cursoopcao2_nome, ");
		sb.append(" turnocursoopcao2.codigo as turnocursoopcao2_codigo, turnocursoopcao2.nome as turnocursoopcao2_nome, ");
		sb.append(" cursoopcao3.codigo as cursoopcao3_codigo, cursoopcao3.nome as cursoopcao3_nome, ");
		sb.append(" turnocursoopcao3.codigo as turnocursoopcao3_codigo, turnocursoopcao3.nome as turnocursoopcao3_nome, ");
		sb.append(" opcaolinguaestrangeira.codigo as opcaolinguaestrangeira_codigo, opcaolinguaestrangeira.nome as opcaolinguaestrangeira_nome,  ");
		sb.append(" sala.codigo as sala_codigo, sala.sala as sala_sala, local.local as local_local ");
		sb.append(" from inscricao ");
		sb.append(" inner join pessoa as candidato on candidato.codigo = inscricao.candidato ");
		sb.append(" inner join procseletivo on procseletivo.codigo = inscricao.procseletivo ");
		sb.append(" inner join unidadeensinocurso as unidadeensinocursoopcao1 on unidadeensinocursoopcao1.codigo = inscricao.cursoopcao1 ");
		if(unidadeEnsinoCurso != null && unidadeEnsinoCurso > 0){
			sb.append(" and unidadeensinocursoopcao1.codigo = ").append(unidadeEnsinoCurso);
		}
		sb.append(" inner join curso as cursoopcao1 on cursoopcao1.codigo = unidadeensinocursoopcao1.curso ");
		sb.append(" inner join turno as turnocursoopcao1 on turnocursoopcao1.codigo = unidadeensinocursoopcao1.turno ");
		sb.append(" left join SalaLocalAula as sala on sala.codigo = inscricao.sala ");
		sb.append(" left join LocalAula as local on local.codigo = sala.LocalAula ");

		sb.append(" inner join ItemProcSeletivoDataProva on ");
		sb.append(" ItemProcSeletivoDataProva.codigo =  inscricao.itemProcessoSeletivoDataProva ");
		if (dataProvra != null && dataProvra > 0) {
			sb.append(" and ItemProcSeletivoDataProva.codigo = ").append(dataProvra);
		}
		sb.append(" left join unidadeensinocurso as unidadeensinocursoopcao2 on unidadeensinocursoopcao2.codigo = inscricao.cursoopcao2 ");
		sb.append(" left join curso as cursoopcao2 on cursoopcao2.codigo = unidadeensinocursoopcao2.curso ");
		sb.append(" left join turno as turnocursoopcao2 on turnocursoopcao2.codigo = unidadeensinocursoopcao2.turno ");
		sb.append(" left join unidadeensinocurso as unidadeensinocursoopcao3 on unidadeensinocursoopcao3.codigo = inscricao.cursoopcao3 ");
		sb.append(" left join curso as cursoopcao3 on cursoopcao3.codigo = unidadeensinocursoopcao3.curso ");
		sb.append(" left join turno as turnocursoopcao3 on turnocursoopcao3.codigo = unidadeensinocursoopcao3.turno ");
		sb.append(" left join disciplinasprocseletivo as opcaolinguaestrangeira on opcaolinguaestrangeira.codigo = inscricao.opcaolinguaestrangeira ");
		sb.append(" where 1=1 ");
		sb.append("  ").append(adicionarFiltroRelatorioProcessoSeletivo(filtroRelatorioProcessoSeletivo, "inscricao"));
		if (processoSeletivo > 0) {
			sb.append(" and procseletivo.codigo = ").append(processoSeletivo);
		}
		if (!ano.equals("")) {
			sb.append(" and procseletivo.ano = '").append(ano).append("'");
		}
		if (!semestre.equals("")) {
			sb.append(" and procseletivo.semestre = '").append(semestre).append("'");
		}
		if (dataProvra != null && dataProvra > 0 && sala != null && sala > 0) {
			
			sb.append(" and  sala.codigo =  ").append(sala);
		}
		if (dataProvra != null && dataProvra > 0 && sala != null && sala < 0) {			
			sb.append(" and  inscricao.sala is null  ");
		}
		switch (tipoRelatorio) {
		case LISTAGEM_APROVADOS:
			sb.append(" AND EXISTS (select from resultadoprocessoseletivo rps where resultadoprimeiraopcao = 'AP' and rps.inscricao = inscricao.codigo ) ");
			break;
		case LISTAGEM_FREQUENCIA:
			sb.append(" and inscricao.formaingresso = 'PS' ");
			break;
		case LISTAGEM_PRESENTES:
			sb.append(" AND inscricao.formaingresso = 'PS' ");
			sb.append(" AND EXISTS (select from resultadoprocessoseletivo rps where rps.inscricao = inscricao.codigo ) ");
			break;
		case LISTAGEM_AUSENTES:
			sb.append(" and inscricao.formaingresso = 'PS' ");
			sb.append(" AND NOT EXISTS (select from resultadoprocessoseletivo rps where rps.inscricao = inscricao.codigo ) ");
			break;
		case LISTAGEM_MATRICULADOS:
			sb.append(" and EXISTS (select from resultadoprocessoseletivo rps where rps.resultadoprimeiraopcao = 'AP' and rps.inscricao = inscricao.codigo ) ");
			sb.append(" and EXISTS (select from matricula where matricula.inscricao =  inscricao.codigo ) ");
			break;
		case LISTAGEM_NAO_MATRICULADOS:
			sb.append(" AND EXISTS (select from resultadoprocessoseletivo rps where rps.resultadoprimeiraopcao = 'AP' and rps.inscricao = inscricao.codigo ) ");
			sb.append(" AND NOT EXISTS (select from matricula where matricula.inscricao = inscricao.codigo ) ");
			break;
		case LISTAGEM_REPROVADOS:
			sb.append(" AND NOT EXISTS (select from resultadoprocessoseletivo rps where rps.resultadoprimeiraopcao = 'RE' and rps.inscricao = inscricao.codigo ) ");
			break;
		case LEMBRETE_DATA_PROVA:
			sb.append(" and inscricao.formaingresso = 'PS' ");
			sb.append(" and ItemProcSeletivoDataProva.dataProva > current_timestamp ");
			if(qtdeDiasNotificarDataProva > 0) {
				sb.append(" and ItemProcSeletivoDataProva.dataProva::Date = (current_date + ").append(qtdeDiasNotificarDataProva).append(") ");
			}
		default:
			break;
		}
		sb.append(" order by candidato.nome ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (rs.next()) {
			InscricaoVO inscricaoVO = null;
			inscricaoVO = new InscricaoVO();
			inscricaoVO.setCodigo(rs.getInt("codigo"));
			inscricaoVO.getCandidato().setCodigo(rs.getInt("candidato_codigo"));
			if (apresentarNomeCandidatoCaixaAlta) {
				inscricaoVO.getCandidato().setNome(rs.getString("candidato_nome").toUpperCase());
			} else {
				inscricaoVO.getCandidato().setNome(rs.getString("candidato_nome"));
			}
			inscricaoVO.getCandidato().setEmail(rs.getString("candidato_email"));
			inscricaoVO.getCandidato().setRG(rs.getString("candidato_rg"));
			inscricaoVO.getCandidato().setCPF(rs.getString("candidato_cpf"));
			inscricaoVO.getCandidato().setTelefoneComer(rs.getString("candidato_telefoneComer"));
			inscricaoVO.getCandidato().setTelefoneRecado(rs.getString("candidato_telefoneRecado"));
			inscricaoVO.getCandidato().setTelefoneRes(rs.getString("candidato_telefoneRes"));
			inscricaoVO.getCandidato().setCelular(rs.getString("candidato_celular"));
			inscricaoVO.getCandidato().setSexo(rs.getString("candidato_sexo"));
			inscricaoVO.getProcSeletivo().setCodigo(rs.getInt("procseletivo_codigo"));
			inscricaoVO.getProcSeletivo().setDescricao(rs.getString("procseletivo_descricao").toUpperCase());
			inscricaoVO.getOpcaoLinguaEstrangeira().setCodigo(rs.getInt("opcaolinguaestrangeira_codigo"));
			inscricaoVO.getItemProcessoSeletivoDataProva().setDataProva(rs.getDate("ItemProcSeletivoDataProva_dataProva"));
			if (rs.getString("opcaolinguaestrangeira_nome") != null) {
				inscricaoVO.getOpcaoLinguaEstrangeira().setNome(rs.getString("opcaolinguaestrangeira_nome").toUpperCase());
			}
			inscricaoVO.getCursoOpcao1().getCurso().setCodigo(rs.getInt("cursoopcao1_codigo"));
			inscricaoVO.getCursoOpcao1().getCurso().setNome(rs.getString("cursoopcao1_nome").toUpperCase());
			inscricaoVO.getCursoOpcao1().getTurno().setCodigo(rs.getInt("turnocursoopcao1_codigo"));
			inscricaoVO.getCursoOpcao1().getTurno().setNome(rs.getString("turnocursoopcao1_nome").toUpperCase());
			if (rs.getInt("cursoopcao2_codigo") > 0) {
				inscricaoVO.getCursoOpcao2().getCurso().setCodigo(rs.getInt("cursoopcao2_codigo"));
				inscricaoVO.getCursoOpcao2().getTurno().setNome(rs.getString("cursoopcao2_nome").toUpperCase());
				inscricaoVO.getCursoOpcao2().getTurno().setCodigo(rs.getInt("turnocursoopcao2_codigo"));
				inscricaoVO.getCursoOpcao2().getCurso().setNome(rs.getString("turnocursoopcao2_nome").toUpperCase());
			}
			if (rs.getInt("cursoopcao3_codigo") > 0) {
				inscricaoVO.getCursoOpcao3().getCurso().setCodigo(rs.getInt("cursoopcao3_codigo"));
				inscricaoVO.getCursoOpcao3().getCurso().setNome(rs.getString("cursoopcao3_nome").toUpperCase());
				inscricaoVO.getCursoOpcao3().getTurno().setCodigo(rs.getInt("turnocursoopcao3_codigo"));
				inscricaoVO.getCursoOpcao3().getTurno().setNome(rs.getString("turnocursoopcao3_nome").toUpperCase());
			}
			inscricaoVO.getSala().setCodigo(rs.getInt("sala_codigo"));
			inscricaoVO.getSala().setSala(rs.getString("sala_sala"));
			inscricaoVO.getSala().getLocalAula().setLocal(rs.getString("local_local"));
			inscricaoVO.setNovoObj(false);
			inscricaoVOs.add(inscricaoVO);
		}
		return inscricaoVOs;
	}

	private List<EstatisticaProcessoSeletivoVO> consultarDadosAusentesPresentesCursoTurnoData(Integer processoSeletivo, Integer dataProvra, Integer sala, Integer unidadeEnsinoCurso, String ano, String semestre, FiltroRelatorioProcessoSeletivoVO filtroRelatorioProcessoSeletivo) throws Exception {
		StringBuilder sb = new StringBuilder(" SELECT 		 cursoopcao1.codigo as cursoopcao1_codigo, cursoopcao1.nome as cursoopcao1_nome, ");
		sb.append(" turnocursoopcao1.codigo as turnocursoopcao1_codigo, turnocursoopcao1.nome as turnocursoopcao1_nome, ");
		sb.append("  ItemProcSeletivoDataProva.dataProva,  ");
		sb.append(" sum(case when inscricao.codigo not in (select rps.inscricao from resultadoprocessoseletivo rps where rps.inscricao =  inscricao.codigo ) then 1 else 0 end) as ausentes, ");
		sb.append(" sum(case when inscricao.codigo in (select rps.inscricao from resultadoprocessoseletivo rps where rps.inscricao =  inscricao.codigo ) then 1 else 0 end) as presentes  ");
		sb.append(" from inscricao  ");
		sb.append(" inner join pessoa as candidato on candidato.codigo = inscricao.candidato ");
		sb.append(" inner join procseletivo on procseletivo.codigo = inscricao.procseletivo  ");
		sb.append(" inner join unidadeensinocurso as unidadeensinocursoopcao1 on unidadeensinocursoopcao1.codigo = inscricao.cursoopcao1 ");
		if(unidadeEnsinoCurso != null && unidadeEnsinoCurso > 0){
			sb.append(" and unidadeensinocursoopcao1.codigo = ").append(unidadeEnsinoCurso);
		}
		sb.append(" inner join curso as cursoopcao1 on cursoopcao1.codigo = unidadeensinocursoopcao1.curso  ");
		sb.append(" inner join turno as turnocursoopcao1 on turnocursoopcao1.codigo = unidadeensinocursoopcao1.turno ");
		sb.append(" inner join ItemProcSeletivoDataProva on ItemProcSeletivoDataProva.codigo =  inscricao.itemProcessoSeletivoDataProva ");
		if (dataProvra != null && dataProvra > 0) {
			sb.append(" and ItemProcSeletivoDataProva.codigo = ").append(dataProvra);
		}
		sb.append(" left join SalaLocalAula as sala on sala.codigo = inscricao.sala  ");
		sb.append(" left join LocalAula as local on local.codigo = sala.LocalAula ");
		sb.append("  where 1=1 ");
		sb.append("  ").append(adicionarFiltroRelatorioProcessoSeletivo(filtroRelatorioProcessoSeletivo, "inscricao"));		
		if (processoSeletivo > 0) {
			sb.append("  and procseletivo.codigo = ").append(processoSeletivo);			
		}		
		if (!ano.equals("")) {
			sb.append(" and procseletivo.ano = '").append(ano).append("'");
		}
		if (!semestre.equals("")) {
			sb.append(" and procseletivo.semestre = '").append(semestre).append("'");
		}	
		sb.append(" and inscricao.formaingresso = 'PS' ");
		sb.append(" and  inscricao.situacao = 'CO' ");
		if (dataProvra != null && dataProvra > 0 && sala != null && sala > 0) {
			sb.append(" and  sala.codigo =  ").append(sala);
		}
		if (dataProvra != null && dataProvra > 0 && sala != null && sala < 0) {			
			sb.append(" and  inscricao.sala is null  ");
		}
		sb.append(" group by cursoopcao1.codigo, cursoopcao1.nome, turnocursoopcao1.codigo, turnocursoopcao1.nome, ItemProcSeletivoDataProva.dataProva ");
		sb.append(" order by ItemProcSeletivoDataProva.dataProva, cursoopcao1.nome, turnocursoopcao1.nome ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<EstatisticaProcessoSeletivoVO> estatisticaProcessoSeletivoVOs = new ArrayList<EstatisticaProcessoSeletivoVO>(0);
		EstatisticaProcessoSeletivoVO estatisticaProcessoSeletivoVO = null;
		while (rs.next()) {

			estatisticaProcessoSeletivoVO = new EstatisticaProcessoSeletivoVO();
			estatisticaProcessoSeletivoVO.setCurso(rs.getString("cursoopcao1_nome") + " / " + rs.getString("turnocursoopcao1_nome"));
			estatisticaProcessoSeletivoVO.setTurno(rs.getString("turnocursoopcao1_nome"));
			estatisticaProcessoSeletivoVO.setQuantidadeAusente(rs.getInt("ausentes"));
			estatisticaProcessoSeletivoVO.setQuantidadePresente(rs.getInt("presentes"));
			estatisticaProcessoSeletivoVO.setDataProva(rs.getDate("dataProva"));
			estatisticaProcessoSeletivoVOs.add(estatisticaProcessoSeletivoVO);

		}
		return estatisticaProcessoSeletivoVOs;
	}

	private List<EstatisticaProcessoSeletivoVO> consultarDadosEstatisticosPorBairro(Integer processoSeletivo, Integer dataProvra, Integer sala, Integer unidadeEnsinoCurso, String ano, String semestre, FiltroRelatorioProcessoSeletivoVO filtroRelatorioProcessoSeletivo, Boolean apresentarNomeCandidatoCaixaAlta) throws Exception {

		StringBuilder sb = new StringBuilder(" select cidade.nome, upper(trim(sem_acentos(setor))) as setor, count(inscricao.codigo) as quantidade from inscricao");
		sb.append(" inner join pessoa as candidato on candidato.codigo = inscricao.candidato");
		sb.append(" inner join unidadeensinocurso as unidadeensinocursoopcao1 on unidadeensinocursoopcao1.codigo = inscricao.cursoopcao1 ");
		if(unidadeEnsinoCurso != null && unidadeEnsinoCurso > 0){
			sb.append(" and unidadeensinocursoopcao1.codigo = ").append(unidadeEnsinoCurso);
		}
		sb.append(" inner join procseletivo on procseletivo.codigo = inscricao.procseletivo ");
		sb.append(" left join cidade on candidato.cidade = cidade.codigo");
		sb.append(" inner join ItemProcSeletivoDataProva on ");
		sb.append(" ItemProcSeletivoDataProva.codigo =  inscricao.itemProcessoSeletivoDataProva ");
		if (dataProvra != null && dataProvra > 0) {
			sb.append(" and ItemProcSeletivoDataProva.codigo = ").append(dataProvra);
		}
		sb.append("  where 1=1 ");
		sb.append("  ").append(adicionarFiltroRelatorioProcessoSeletivo(filtroRelatorioProcessoSeletivo, "inscricao"));	
		if (processoSeletivo > 0) {
			sb.append("  and procseletivo.codigo = ").append(processoSeletivo);			
		}		
		if (!ano.equals("")) {
			sb.append(" and procseletivo.ano = '").append(ano).append("'");
		}
		if (!semestre.equals("")) {
			sb.append(" and procseletivo.semestre = '").append(semestre).append("'");
		}			
		if (dataProvra != null && dataProvra > 0 && sala != null && sala > 0) {
			sb.append(" and  inscricao.sala =  ").append(sala);
		}
		if (dataProvra != null && dataProvra > 0 && sala != null && sala < 0) {			
			sb.append(" and  inscricao.sala is null  ");
		}
		sb.append(" group by cidade.nome, upper(trim(sem_acentos(setor)))");
		sb.append(" order by nome, setor");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<EstatisticaProcessoSeletivoVO> estatisticaProcessoSeletivoVOs = new ArrayList<EstatisticaProcessoSeletivoVO>(0);
		EstatisticaProcessoSeletivoVO obj = null;
		while (rs.next()) {
			obj = new EstatisticaProcessoSeletivoVO();
			obj.setQuantidade(rs.getInt("quantidade"));
			if (rs.getString("nome") != null) {
				if (apresentarNomeCandidatoCaixaAlta) {
					obj.setDescricao(rs.getString("nome").toUpperCase());
				} else {
					obj.setDescricao(rs.getString("nome"));
				}
			}
			obj.setDescricaoComplementar(rs.getString("setor"));
			estatisticaProcessoSeletivoVOs.add(obj);
		}
		return estatisticaProcessoSeletivoVOs;
	}

	private List<EstatisticaProcessoSeletivoVO> consultarDadosEstatisticosPorCurso(Integer processoSeletivo, Integer dataProvra, Integer sala, Integer unidadeEnsinoCurso, String ano, String semestre, FiltroRelatorioProcessoSeletivoVO filtroRelatorioProcessoSeletivo) throws Exception {
		StringBuilder sb = new StringBuilder(" select cursoopcao1.nome as curso, turnocursoopcao1.nome as turno, count(inscricao.codigo) as quantidade from inscricao");
		sb.append(" inner join unidadeensinocurso as unidadeensinocursoopcao1 on unidadeensinocursoopcao1.codigo = inscricao.cursoopcao1 ");
		if(unidadeEnsinoCurso != null && unidadeEnsinoCurso > 0){
			sb.append(" and unidadeensinocursoopcao1.codigo = ").append(unidadeEnsinoCurso);
		}
		sb.append(" inner join curso as cursoopcao1 on cursoopcao1.codigo = unidadeensinocursoopcao1.curso ");
		sb.append(" inner join turno as turnocursoopcao1 on turnocursoopcao1.codigo = unidadeensinocursoopcao1.turno ");
		sb.append(" inner join procseletivo on procseletivo.codigo = inscricao.procseletivo ");
		sb.append(" inner join ItemProcSeletivoDataProva on ");
		sb.append(" ItemProcSeletivoDataProva.codigo =  inscricao.itemProcessoSeletivoDataProva ");
		if (dataProvra != null && dataProvra > 0) {
			sb.append(" and ItemProcSeletivoDataProva.codigo = ").append(dataProvra);
		}
		if (dataProvra != null && dataProvra > 0 && sala != null && sala > 0) {
			sb.append(" and  inscricao.sala =  ").append(sala);
		}
		if (dataProvra != null && dataProvra > 0 && sala != null && sala < 0) {			
			sb.append(" and  inscricao.sala is null  ");
		}
		sb.append("  where 1=1 ");
		sb.append("  ").append(adicionarFiltroRelatorioProcessoSeletivo(filtroRelatorioProcessoSeletivo, "inscricao"));	
		if (processoSeletivo > 0) {
			sb.append("  and procseletivo.codigo = ").append(processoSeletivo);			
		}		
		if (!ano.equals("")) {
			sb.append(" and procseletivo.ano = '").append(ano).append("'");
		}
		if (!semestre.equals("")) {
			sb.append(" and procseletivo.semestre = '").append(semestre).append("'");
		}			
		sb.append(" group by cursoopcao1.nome, turnocursoopcao1.nome");
		sb.append(" order by cursoopcao1.nome, turnocursoopcao1.nome");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<EstatisticaProcessoSeletivoVO> estatisticaProcessoSeletivoVOs = new ArrayList<EstatisticaProcessoSeletivoVO>(0);
		EstatisticaProcessoSeletivoVO obj = null;
		while (rs.next()) {
			obj = new EstatisticaProcessoSeletivoVO();
			obj.setQuantidade(rs.getInt("quantidade"));
			obj.setDescricao(rs.getString("curso").toUpperCase());
			obj.setDescricaoComplementar(rs.getString("turno").toUpperCase());
			estatisticaProcessoSeletivoVOs.add(obj);
		}
		return estatisticaProcessoSeletivoVOs;
	}
	
	private List<EstatisticaProcessoSeletivoVO> consultarDadosMuralAluno(Integer processoSeletivo, Integer dataProva, Integer sala, Integer unidadeEnsinoCurso, String ano, String semestre, FiltroRelatorioProcessoSeletivoVO filtroRelatorioProcessoSeletivo, Boolean apresentarNomeCandidatoCaixaAlta) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT ItemProcSeletivoDataProva.dataProva, unidadeensino.nome as unidadeensino, procseletivo.descricao as processoseletivo, sala.sala, ");
		sqlStr.append(" inscricao.codigo as numeroInscricao, candidato.nome as nomeCandidato, local.local ");
		sqlStr.append(" from inscricao ");
		sqlStr.append(" inner join pessoa as candidato on candidato.codigo = inscricao.candidato ");
		sqlStr.append(" inner join procseletivo on procseletivo.codigo = inscricao.procseletivo ");
		sqlStr.append(" inner join unidadeensinocurso on unidadeensinocurso.codigo = inscricao.cursoopcao1 ");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = unidadeensinocurso.unidadeensino ");
		sqlStr.append(" inner join ItemProcSeletivoDataProva on ItemProcSeletivoDataProva.codigo =  inscricao.itemProcessoSeletivoDataProva ");
		sqlStr.append(" left join SalaLocalAula as sala on sala.codigo = inscricao.sala ");
		sqlStr.append(" left join LocalAula as local on local.codigo = sala.LocalAula ");
		sqlStr.append("  where 1=1 ");
		sqlStr.append("  ").append(adicionarFiltroRelatorioProcessoSeletivo(filtroRelatorioProcessoSeletivo, "inscricao"));	
		if (processoSeletivo > 0) {
			sqlStr.append("  and procseletivo.codigo = ").append(processoSeletivo);			
		}
		sqlStr.append(" and inscricao.formaingresso = 'PS' ");
		if (!ano.equals("")) {
			sqlStr.append(" and procseletivo.ano = '").append(ano).append("'");
		}
		if (!semestre.equals("")) {
			sqlStr.append(" and procseletivo.semestre = '").append(semestre).append("'");
		}		
		if (dataProva != null && dataProva > 0) {
			sqlStr.append(" and ItemProcSeletivoDataProva.codigo = ").append(dataProva);
		}
		if (dataProva != null && dataProva > 0 && sala != null && sala > 0) {
			sqlStr.append(" and  inscricao.sala =  ").append(sala);
		}
		if (dataProva != null && dataProva > 0 && sala != null && sala < 0) {			
			sqlStr.append(" and  inscricao.sala is null  ");
		}
		if(unidadeEnsinoCurso != null && unidadeEnsinoCurso > 0){
			sqlStr.append(" and unidadeensinocurso.codigo = ").append(unidadeEnsinoCurso);
		}
		sqlStr.append(" order by sala, local, candidato.nome ");
		return montarDadosConsultaMuralAluno(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()), apresentarNomeCandidatoCaixaAlta);
	}
	
	private List<EstatisticaProcessoSeletivoVO> montarDadosConsultaMuralAluno(SqlRowSet rs, Boolean apresentarNomeCandidatoCaixaAlta) {
		List<EstatisticaProcessoSeletivoVO> estatisticaProcessoSeletivoVOs = new ArrayList<EstatisticaProcessoSeletivoVO>(0);
		EstatisticaProcessoSeletivoVO obj = null;
		while (rs.next()) {
			obj = new EstatisticaProcessoSeletivoVO();
			obj.setDataProva(rs.getDate("dataProva"));
			if (rs.getString("local") != null) {
				obj.setSala(rs.getString("sala") + " / " + rs.getString("local"));
			} else {
				obj.setSala(rs.getString("sala"));
			}
			obj.setNumeroInscricao(rs.getString("numeroInscricao"));
			if (apresentarNomeCandidatoCaixaAlta) {
				obj.setNomeCandidato(rs.getString("nomeCandidato").toUpperCase());
			} else {
				obj.setNomeCandidato(rs.getString("nomeCandidato"));
			}
			estatisticaProcessoSeletivoVOs.add(obj);
		}
		return estatisticaProcessoSeletivoVOs;
	}
	
	public StringBuilder adicionarFiltroRelatorioProcessoSeletivo(FiltroRelatorioProcessoSeletivoVO filtroRelatorioProcessoSeletivoVO, String keyEntidade) {
		StringBuilder sqlStr = new StringBuilder("");
		keyEntidade = keyEntidade.trim();
		if (filtroRelatorioProcessoSeletivoVO.getAtivo() || filtroRelatorioProcessoSeletivoVO.getCancelado() || filtroRelatorioProcessoSeletivoVO.getCanceladoOutraInscricao() || filtroRelatorioProcessoSeletivoVO.getNaoCompareceu()) {
			sqlStr.append(" and ").append(keyEntidade).append(".situacaoInscricao in (");
			boolean virgula = false;
			if (filtroRelatorioProcessoSeletivoVO.getAtivo()) {
				sqlStr.append(virgula ? "," : "").append("'").append(SituacaoInscricaoEnum.ATIVO).append("'");
				virgula = true;
			}
			if (filtroRelatorioProcessoSeletivoVO.getCancelado()) {
				sqlStr.append(virgula ? "," : "").append("'").append(SituacaoInscricaoEnum.CANCELADO).append("'");
				virgula = true;
			}
			if (filtroRelatorioProcessoSeletivoVO.getCanceladoOutraInscricao()) {
				sqlStr.append(virgula ? "," : "").append("'").append(SituacaoInscricaoEnum.CANCELADO_OUTRA_INSCRICAO).append("'");
				virgula = true;
			}
			if (filtroRelatorioProcessoSeletivoVO.getNaoCompareceu()) {
				sqlStr.append(virgula ? "," : "").append("'").append(SituacaoInscricaoEnum.NAO_COMPARECEU).append("'");
				virgula = true;
			}
			sqlStr.append(" ) ");
		}
		if (filtroRelatorioProcessoSeletivoVO.getConfirmado() || filtroRelatorioProcessoSeletivoVO.getPendenteFinanceiro()) {
			sqlStr.append(" and ").append(keyEntidade).append(".situacao in ( ");
			boolean virgula = false;
			if (filtroRelatorioProcessoSeletivoVO.getConfirmado()) {
				sqlStr.append(virgula ? "," : "").append("'CO'");
				virgula = true;
			}
			if (filtroRelatorioProcessoSeletivoVO.getPendenteFinanceiro()) {
				sqlStr.append(virgula ? "," : "").append("'PF'");
				virgula = true;
			}
			sqlStr.append(" ) ");
		}
		
		if (filtroRelatorioProcessoSeletivoVO.getProcessoSeletivo() || filtroRelatorioProcessoSeletivoVO.getPortadorDiploma() || filtroRelatorioProcessoSeletivoVO.getEnem()) {
			sqlStr.append(" and ").append(keyEntidade).append(".formaIngresso in (");
			boolean virgula = false;
			if (filtroRelatorioProcessoSeletivoVO.getProcessoSeletivo()) {
				sqlStr.append(virgula ? "," : "").append("'PS'");
				virgula = true;
			}
			if (filtroRelatorioProcessoSeletivoVO.getPortadorDiploma()) {
				sqlStr.append(virgula ? "," : "").append("'PD'");
				virgula = true;
			}
			if (filtroRelatorioProcessoSeletivoVO.getEnem()) {
				sqlStr.append(virgula ? "," : "").append("'EN'");
				virgula = true;
			}
			if (filtroRelatorioProcessoSeletivoVO.getTransferencia()) {
				sqlStr.append(virgula ? "," : "").append("'TR'");
				virgula = true;
			}
			
			sqlStr.append(" ) ");
		}
		if(!sqlStr.toString().isEmpty()){
			return sqlStr; 
		}
		return new StringBuilder(" and 1=1 ");

	}

	@Override
	public String getDesignIReportRelatorio(TipoRelatorioEstatisticoProcessoSeletivoEnum tipoRelatorio) {
		switch (tipoRelatorio) {
		case LISTAGEM_APROVADOS:
			return getDesignerListaInscricao2();
		case LISTAGEM_AUSENTES:
			return getDesignerListaInscricao2();
		case LISTAGEM_MATRICULADOS:
			return getDesignerListaInscricao();
		case LISTAGEM_NAO_MATRICULADOS:
			return getDesignerListaInscricao2();
		case LISTAGEM_REPROVADOS:
			return getDesignerListaInscricao2();
		case LISTAGEM_FREQUENCIA:
			return getDesignerListaPresenca();
		case DADOS_CANDIDATOS:
			return getDesignerListaDadosCandidato();
		case INSCRITOS_BAIRRO:
			return getDesignerListaEstatistica();
		case INSCRITOS_CURSO:
			return getDesignerListaEstatistica();
		case LISTAGEM_PRESENTE_AUSENTES_CURSO_TURNO_DATA:
			return getDesignerListaEstatisticaAusentePresenteCursoTurnoData();
		case LISTAGEM_CLASSIFICADOS:
			return getDesignerListaClassificacao();
		case LISTAGEM_MURAL_CANDIDATO:
			return getDesignerListaMuralCandidato();
		case LISTAGEM_CANDIDATOS_CHAMADOS:
			return getDesignerListaChamados();
		default:
			return getDesignerListaInscricao();
		}
	}

	private String getDesignerListaEstatistica() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator + "ListaEstatisticaProcessoSeletivo.jrxml");
	}

	private String getDesignerListaEstatisticaAusentePresenteCursoTurnoData() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator + "ListaEstatisticaProcessoSeletivoAusentePresenteCursoTurnoData.jrxml");
	}

	private String getDesignerListaDadosCandidato() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator + "ListaDadoCandidatoProcessoSeletivo.jrxml");
	}

	private String getDesignerListaInscricao() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator + "ListaInscricaoProcessoSeletivo.jrxml");
	}

	private String getDesignerListaInscricao2() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator + "ListaInscricaoProcessoSeletivo2.jrxml");
	}
	
	private String getDesignerListaClassificacao() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator + "ListaClassificacaoProcessoSeletivo.jrxml");
	}
	
	private String getDesignerListaChamados() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator + "ListaChamadosProcessoSeletivo.jrxml");
	}

	private String getDesignerListaPresenca() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator + "ListaPresencaProcessoSeletivo.jrxml");
	}
	
	private String getDesignerListaMuralCandidato() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator + "ListaEstatisticaProcessoSeletivoMuralCandidato.jrxml");
	}

	@Override
	public String caminhoBaseIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator);
	}

	public static String getIdEntidade() {
		return ("EstatisticaProcessoSeletivoRel");
	}
}