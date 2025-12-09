package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.enumeradores.TagsMensagemAutomaticaEnum;
import negocio.comuns.administrativo.enumeradores.TemplateMensagemAutomaticaEnum;
import negocio.comuns.administrativo.enumeradores.TipoOrigemComunicacaoInternaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import relatorio.negocio.comuns.academico.AniversariantesDoMesRelVO;
import relatorio.negocio.interfaces.academico.AniversariantesDoMesRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class AniversariantesDoMesRel extends SuperRelatorio implements AniversariantesDoMesRelInterfaceFacade {

	public AniversariantesDoMesRel() {
	}

	public void validarDados(Integer unidadeEnsino, String identificarTurma) throws Exception {
		if (unidadeEnsino == 0) {
			throw new Exception("O campo UNIDADE DE ENSINO deve ser informado.");
		}
		if (identificarTurma.equals("")) {
			throw new Exception("O campo TURMA deve ser informado.");
		}
	}

	public List<AniversariantesDoMesRelVO> criarObjeto(Integer codUnidade, TurmaVO turmaVO, String mes, String dia, String diaFim, UsuarioVO usuario, boolean aluno, boolean funcionario,FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) throws Exception {
		List<AniversariantesDoMesRelVO> listaRelatorio = new ArrayList<AniversariantesDoMesRelVO>(0);
		if (aluno){
            listaRelatorio = executarConsultaAluno(codUnidade, turmaVO, mes, dia, diaFim, usuario,filtroRelatorioAcademicoVO);
		} else if (funcionario){
			listaRelatorio = executarConsultaFuncionario(codUnidade, mes, dia, diaFim, usuario);            
		} else {
			listaRelatorio = executarConsultaProfessor(codUnidade, mes, dia, diaFim, usuario);
		}
		return listaRelatorio;
	}

	public List<AniversariantesDoMesRelVO> executarConsultaAluno(Integer unidade, TurmaVO turmaVO, String mes, String dia, String diaFim, UsuarioVO usuario,FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT DISTINCT 'AL' AS tipopessoa, ");
		sqlStr.append(" pessoa.setor as bairro,pessoa.cep,pessoa.complemento,cidade.nome AS cidade, pessoa.numero, ");
		sqlStr.append(" estado.sigla as estado,pessoa.telefoneres as telefone ,pessoa.endereco, ");
		sqlStr.append(" pessoa.codigo as pessoa,pessoa.celular,pessoa.aluno, turma.identificadorTurma, matricula.matricula, ");
		sqlStr.append(" pessoa.nome, pessoa.email, pessoa.email2, to_char(pessoa.datanasc,\'DD/MM/YYYY\') as dataNasc ");
		sqlStr.append("FROM matriculaperiodo ");
		sqlStr.append("INNER JOIN turma     ON turma.codigo        = matriculaperiodo.turma ");
		sqlStr.append("INNER JOIN matricula ON matricula.matricula = matriculaperiodo.matricula ");
		sqlStr.append("INNER JOIN pessoa    ON pessoa.codigo       = matricula.aluno ");
		sqlStr.append("LEFT JOIN cidade     ON pessoa.cidade       = cidade.codigo ");
		sqlStr.append("LEFT JOIN estado     ON cidade.estado       = estado.codigo ");
		sqlStr.append("WHERE 1=1 ");
		sqlStr.append(" AND ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));
		
		if(Uteis.isAtributoPreenchido(turmaVO.getCurso().getCodigo())){
			sqlStr.append(" AND matricula.curso = ").append(turmaVO.getCurso().getCodigo().intValue());
		}
		if (Uteis.isAtributoPreenchido(turmaVO)) {
			sqlStr.append(" AND turma.codigo = ").append(turmaVO.getCodigo().intValue());
		}
		if (Uteis.isAtributoPreenchido(unidade)) {
			sqlStr.append(" AND matricula.unidadeensino = ").append(unidade);
		}
		sqlStr.append(" AND matriculaperiodo.codigo IN (SELECT mp.codigo  FROM matriculaperiodo AS mp WHERE mp.matricula = matriculaperiodo.matricula ");
		sqlStr.append("ORDER BY mp.ano || '/' || mp.semestre DESC, mp.codigo DESC LIMIT 1) ");
		
		sqlStr.append(" AND (DATE_PART('MONTH', pessoa.datanasc) = '").append(mes).append("') ");
		sqlStr.append(" AND (DATE_PART('DAY', pessoa.datanasc) >= '").append(dia).append("') ");
		sqlStr.append(" AND (DATE_PART('DAY', pessoa.datanasc) <= '").append(diaFim).append("') ");
		sqlStr.append(" ORDER BY turma.identificadorTurma,  pessoa.nome , TO_CHAR(pessoa.datanasc,'DD/MM/YYYY') ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	public List<AniversariantesDoMesRelVO> executarConsultaProfessor(Integer unidade, String mes, String dia, String diaFim, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT DISTINCT 'PR' AS tipopessoa, ");
		sqlStr.append(" pessoa.setor as bairro,pessoa.cep,pessoa.complemento,cidade.nome AS cidade, pessoa.numero, ");
		sqlStr.append(" estado.sigla as estado,pessoa.telefoneres as telefone ,pessoa.endereco, ");
		sqlStr.append(" pessoa.codigo as pessoa,pessoa.celular,pessoa.aluno,'' AS identificadorTurma, funcionario.matricula, ");
		sqlStr.append(" pessoa.nome, pessoa.email, pessoa.email2, to_char(pessoa.datanasc,\'DD/MM/YYYY\') as dataNasc ");
		sqlStr.append("FROM pessoa ");
		sqlStr.append("INNER JOIN funcionario on funcionario.pessoa = pessoa.codigo	");
		sqlStr.append("LEFT JOIN funcionariocargo on funcionario.codigo = funcionariocargo.funcionario	");
		sqlStr.append("LEFT JOIN cidade     ON pessoa.cidade       = cidade.codigo ");
		sqlStr.append("LEFT JOIN estado     ON cidade.estado       = estado.codigo ");
		sqlStr.append("where 1=1 and professor = true and pessoa.ativo ");
        if(unidade !=  null && unidade>0){
        	sqlStr.append(" AND (funcionariocargo.unidadeensino = ").append(unidade).append(" OR funcionariocargo.unidadeensino is null) ");
        }
		sqlStr.append(" and (date_part('MONTH', pessoa.datanasc) = '").append(mes).append("') ");
		sqlStr.append(" and (date_part('DAY', pessoa.datanasc) >= '").append(dia).append("') ");
		sqlStr.append(" and (date_part('DAY', pessoa.datanasc) <= '").append(diaFim).append("') ");
		sqlStr.append(" order by pessoa.nome , dataNasc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, usuario));
	}
	
	public List<AniversariantesDoMesRelVO> executarConsultaFuncionario(Integer unidade, String mes, String dia, String diaFim, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT DISTINCT 'FU' AS tipopessoa, ");
		sqlStr.append(" pessoa.setor as bairro,pessoa.cep,pessoa.complemento,cidade.nome AS cidade, pessoa.numero, ");
		sqlStr.append(" estado.sigla as estado,pessoa.telefoneres as telefone ,pessoa.endereco, ");
		sqlStr.append(" pessoa.codigo as pessoa,pessoa.celular,pessoa.aluno, '' AS identificadorTurma, funcionario.matricula, ");
		sqlStr.append(" pessoa.nome, pessoa.email, pessoa.email2, to_char(pessoa.datanasc,\'DD/MM/YYYY\') as dataNasc ");
		
		sqlStr.append("from pessoa ");
		sqlStr.append("inner join funcionario on funcionario.pessoa = pessoa.codigo	");
		sqlStr.append("left join funcionariocargo on funcionario.codigo = funcionariocargo.funcionario	");
		sqlStr.append("LEFT JOIN cidade     ON pessoa.cidade       = cidade.codigo ");
		sqlStr.append("LEFT JOIN estado     ON cidade.estado       = estado.codigo ");
		sqlStr.append("where 1=1 and pessoa.funcionario = true and pessoa.ativo ");
        if(unidade !=  null && unidade>0){
        	sqlStr.append(" AND (funcionariocargo.unidadeensino = ").append(unidade).append(" OR funcionariocargo.unidadeensino is null) ");
        }
		sqlStr.append(" and (date_part('MONTH', pessoa.datanasc) = '").append(mes).append("') ");
		sqlStr.append(" and (date_part('DAY', pessoa.datanasc) >= '").append(dia).append("') ");
		sqlStr.append(" and (date_part('DAY', pessoa.datanasc) <= '").append(diaFim).append("') ");
		sqlStr.append(" order by pessoa.nome , dataNasc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	
	public static List<AniversariantesDoMesRelVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<AniversariantesDoMesRelVO> vetResultado = new ArrayList<AniversariantesDoMesRelVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, usuario));
		}
		return vetResultado;
	}

	public static AniversariantesDoMesRelVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		AniversariantesDoMesRelVO obj = new AniversariantesDoMesRelVO();
		obj.setMatricula(dadosSQL.getString("matricula"));
		obj.setCodigoPessoa(dadosSQL.getInt("pessoa"));
		obj.setCelular(dadosSQL.getString("celular"));
		obj.setEmail(dadosSQL.getString("email"));
		obj.setEmail2(dadosSQL.getString("email2"));
		obj.setNomeAluno(dadosSQL.getString("nome"));
		obj.setBairro(dadosSQL.getString("bairro"));
		obj.setCep(dadosSQL.getString("cep"));
		obj.setComplemento(dadosSQL.getString("complemento"));
		obj.setCidade(dadosSQL.getString("cidade"));
		obj.setNumero(dadosSQL.getString("numero"));
		obj.setEstado(dadosSQL.getString("estado"));
		obj.setTelefone(dadosSQL.getString("telefone"));
		obj.setEndereco(dadosSQL.getString("endereco"));
		obj.setCelular(dadosSQL.getString("celular"));
		obj.setTipoPessoa(dadosSQL.getString("tipopessoa"));
		if (dadosSQL.getBoolean("aluno")) {
			obj.setNomeTurma(dadosSQL.getString("identificadorturma"));
		}
		obj.setDataNascimento(dadosSQL.getString("datanasc"));
		
		return obj;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.academico.AniversariantesDoMesRelInterfaceFacade#getDesignIReportRelatorio()
	 */
	public String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadeRel() + ".jrxml");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.academico.AniversariantesDoMesRelInterfaceFacade#getCaminhoBaseRelatorio()
	 */
	public String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public String getIdEntidadeRel() {
		return ("AniversariantesDoMesRel");
	}
	
	public Integer consultarNumeroDestinatario (Integer codUnidade, TurmaVO turmaVO, String mes, String dia, String diaFim, UsuarioVO usuario, boolean aluno, boolean funcionario,FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) throws Exception {
		if (aluno){
           return executarConsultaNumeroDestinatarioAluno(codUnidade, turmaVO, mes, dia, diaFim, usuario,filtroRelatorioAcademicoVO);
		} else if (funcionario){
			return executarConsultaNumeroDestinatarioFuncionario(codUnidade, mes, dia, diaFim, usuario);            
		} else {
			return executarConsultaNumeroDestinatarioProfessor(codUnidade, mes, dia, diaFim, usuario);
		}
	}
	
	public Integer executarConsultaNumeroDestinatarioAluno(Integer unidade, TurmaVO turmaVO, String mes, String dia, String diaFim, UsuarioVO usuario,FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT  COUNT(DISTINCT pessoa.codigo) as quantidade ");
		sqlStr.append("FROM matriculaperiodo ");
		sqlStr.append("INNER JOIN turma     ON turma.codigo        = matriculaperiodo.turma ");
		sqlStr.append("INNER JOIN matricula ON matricula.matricula = matriculaperiodo.matricula ");
		sqlStr.append("INNER JOIN pessoa    ON pessoa.codigo       = matricula.aluno ");
		sqlStr.append("LEFT JOIN cidade     ON pessoa.cidade       = cidade.codigo ");
		sqlStr.append("LEFT JOIN estado     ON cidade.estado       = estado.codigo ");
		sqlStr.append("WHERE 1=1 ");
		sqlStr.append(" AND ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));
		if(Uteis.isAtributoPreenchido(turmaVO.getCurso().getCodigo())){
			sqlStr.append(" AND matricula.curso = ").append(turmaVO.getCurso().getCodigo().intValue());
		}
		if (Uteis.isAtributoPreenchido(turmaVO)) {
			sqlStr.append(" AND turma.codigo = ").append(turmaVO.getCodigo().intValue());
		}
		if (Uteis.isAtributoPreenchido(unidade)) {
			sqlStr.append(" AND matricula.unidadeensino = ").append(unidade);
		}
		sqlStr.append(" AND matriculaperiodo.codigo IN (SELECT mp.codigo  FROM matriculaperiodo AS mp WHERE mp.matricula = matriculaperiodo.matricula ");
		sqlStr.append("ORDER BY mp.ano || '/' || mp.semestre DESC, mp.codigo DESC LIMIT 1) ");
		sqlStr.append(" AND (DATE_PART('MONTH', pessoa.datanasc) = '").append(mes).append("') ");
		sqlStr.append(" AND (DATE_PART('DAY', pessoa.datanasc) >= '").append(dia).append("') ");
		sqlStr.append(" AND (DATE_PART('DAY', pessoa.datanasc) <= '").append(diaFim).append("') ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if(rs.next()){
			return rs.getInt("quantidade");
		}
		return 0;
	}
	
	public Integer executarConsultaNumeroDestinatarioProfessor(Integer unidade, String mes, String dia, String diaFim, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder(
		"SELECT COUNT(DISTINCT pessoa.codigo) as quantidade ");
		sqlStr.append("from pessoa ");
		sqlStr.append("inner join funcionario on funcionario.pessoa = pessoa.codigo	");
		sqlStr.append("left join funcionariocargo on funcionario.codigo = funcionariocargo.funcionario	");
		sqlStr.append("where 1=1 and professor = true and pessoa.ativo ");
        if(unidade !=  null && unidade>0){
        	sqlStr.append(" AND (funcionariocargo.unidadeensino = ").append(unidade).append(" OR funcionariocargo.unidadeensino is null) ");
        }
		sqlStr.append(" and (date_part('MONTH', pessoa.datanasc) = '").append(mes).append("') ");
		sqlStr.append(" and (date_part('DAY', pessoa.datanasc) >= '").append(dia).append("') ");
		sqlStr.append(" and (date_part('DAY', pessoa.datanasc) <= '").append(diaFim).append("') ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if(rs.next()){
			return rs.getInt("quantidade");
		}
		return 0;
	}
	
	public Integer executarConsultaNumeroDestinatarioFuncionario(Integer unidade, String mes, String dia, String diaFim, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder(
		 "SELECT COUNT(DISTINCT pessoa.codigo) as quantidade ");
		sqlStr.append("from pessoa ");
		sqlStr.append("inner join funcionario on funcionario.pessoa = pessoa.codigo	");
		sqlStr.append("left join funcionariocargo on funcionario.codigo = funcionariocargo.funcionario	");
		sqlStr.append("where 1=1 and pessoa.funcionario = true and pessoa.ativo ");
        if(unidade !=  null && unidade>0){
        	sqlStr.append(" AND (funcionariocargo.unidadeensino = ").append(unidade).append(" OR funcionariocargo.unidadeensino is null) ");
        }
		sqlStr.append(" and (date_part('MONTH', pessoa.datanasc) = '").append(mes).append("') ");
		sqlStr.append(" and (date_part('DAY', pessoa.datanasc) >= '").append(dia).append("') ");
		sqlStr.append(" and (date_part('DAY', pessoa.datanasc) <= '").append(diaFim).append("') ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if(rs.next()){
			return rs.getInt("quantidade");
		}
		return 0;
	}
	
	public PersonalizacaoMensagemAutomaticaVO carregarDadosMensagemPersonalizada(UsuarioVO usuarioVO) throws Exception{
		PersonalizacaoMensagemAutomaticaVO personalizacaoMensagemAutomaticaVO = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_ANIVERSARIANTE, false, usuarioVO);
		return personalizacaoMensagemAutomaticaVO;
	}
	
	public String realizarSubstituicaoTagMensagem(AniversariantesDoMesRelVO aniversariantesDoMesRelVO, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_PESSOA.name(),aniversariantesDoMesRelVO.getNomeAluno());
		return mensagemTexto;
	}
	
	public String realizarSubstituicaoTagMensagemSms(AniversariantesDoMesRelVO aniversariantesDoMesRelVO, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_PESSOA.name(), Uteis.getNomeResumidoPessoa(aniversariantesDoMesRelVO.getNomeAluno()));
		if (mensagemTexto.length() > 150) {
			mensagemTexto = mensagemTexto.substring(0, 150);
		}
		return mensagemTexto;
	}
	
    
	public void executarEnvioComunicadoInternoAniversariante(List<AniversariantesDoMesRelVO> listaObjetos,ComunicacaoInternaVO comunicacaoInternaVO,UsuarioVO usuarioVO,ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,Boolean enviarSms,Boolean enviarEmail ) throws Exception{
		 ComunicacaoInternaVO comunicacaoInterna = null;
		    try {

				for (AniversariantesDoMesRelVO obj : listaObjetos) {
					comunicacaoInterna =  (ComunicacaoInternaVO) comunicacaoInternaVO.clone();
					comunicacaoInterna.setTipoOrigemComunicacaoInternaEnum(TipoOrigemComunicacaoInternaEnum.ANIVERSARIANTE);
					comunicacaoInterna.getComunicadoInternoDestinatarioVOs().clear();
					ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO = new ComunicadoInternoDestinatarioVO();
					comunicadoInternoDestinatarioVO.setTipoComunicadoInterno("LE");
					comunicadoInternoDestinatarioVO.setDestinatario(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(obj.getCodigoPessoa(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
				    comunicacaoInterna.setAluno(comunicadoInternoDestinatarioVO.getDestinatario());
					if (Uteis.isAtributoPreenchido(obj.getEmail())) {
						comunicadoInternoDestinatarioVO.setEmail(obj.getEmail());
						comunicadoInternoDestinatarioVO.getDestinatario().setEmail(obj.getEmail());
						comunicacaoInterna.setEnviarEmail(enviarEmail);
					} else if (Uteis.isAtributoPreenchido(obj.getEmail2())) {
						comunicadoInternoDestinatarioVO.setEmail(obj.getEmail2());
						comunicadoInternoDestinatarioVO.getDestinatario().setEmail(obj.getEmail2());
						comunicacaoInterna.setEnviarEmail(enviarEmail);
					} else{
						comunicacaoInterna.setEnviarEmail(false);
					}
					if(Uteis.isAtributoPreenchido(obj.getCelular())){
						comunicadoInternoDestinatarioVO.getDestinatario().setCelular(obj.getCelular());
						comunicacaoInterna.setEnviarSMS(enviarSms);
					}else{
						comunicacaoInterna.setEnviarSMS(false);
					}
					comunicadoInternoDestinatarioVO.setNome(obj.getNomeAluno());
					comunicadoInternoDestinatarioVO.getDestinatario().setNome(obj.getNomeAluno());
					comunicacaoInterna.getComunicadoInternoDestinatarioVOs().add(comunicadoInternoDestinatarioVO);
					comunicacaoInterna.setTipoRemetente("FU");
					comunicacaoInterna.setTipoDestinatario(obj.getTipoPessoa());
					comunicacaoInterna.setTipoComunicadoInterno("LE");
					
			    	if (Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO.getResponsavelPadraoComunicadoInterno().getCodigo())) {
			    	  comunicacaoInterna.setResponsavel(configuracaoGeralSistemaVO.getResponsavelPadraoComunicadoInterno());
				   } else {
					  comunicacaoInterna.setResponsavel(usuarioVO.getPessoa());
				   }
					comunicacaoInterna.setMensagem(getFacadeFactory().getAniversariantesDoMesRelFacade().realizarSubstituicaoTagMensagem(obj, comunicacaoInterna.getMensagem()));
					comunicacaoInterna.setMensagemSMS(getFacadeFactory().getAniversariantesDoMesRelFacade().realizarSubstituicaoTagMensagemSms(obj, comunicacaoInterna.getMensagemSMS()));			
					getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoInterna, false, usuarioVO, configuracaoGeralSistemaVO,null);
					comunicadoInternoDestinatarioVO = null;
					comunicacaoInterna = null;
				}
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}
  	}
	
 
	
	
}
