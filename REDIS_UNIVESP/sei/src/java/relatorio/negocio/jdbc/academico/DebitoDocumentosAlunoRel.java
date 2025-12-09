package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.academico.DebitoDocumentosAlunoRelVO;
import relatorio.negocio.interfaces.academico.DebitoDocumentosAlunoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class DebitoDocumentosAlunoRel extends SuperRelatorio implements DebitoDocumentosAlunoRelInterfaceFacade {


    public DebitoDocumentosAlunoRel() {
        inicializarOrdenacoesRelatorio();
    }

    /*
     * (non-Javadoc)
     *
     * @see relatorio.negocio.jdbc.academico.DebitoDocumentosAlunoRelInterfaceFacade#emitirRelatorio(java.lang.Boolean)
     */
    public List<DebitoDocumentosAlunoRelVO> criarObjeto(Boolean preMatricula, DebitoDocumentosAlunoRelVO debitoDocumentoAlunoRelVO, MatriculaPeriodoVO matriculaperiodoVO, Date dataInicio, Date dataFim, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO, String ano, String semestre, Boolean filtrarCursoAnual, Boolean filtrarCursoSemestral, Boolean filtrarCursoIntegral,
            Boolean documentoEntregue, Boolean documentoPendente, Boolean documentoEntregaIndeferida, Boolean documentoPendenteAprovacao, List<UnidadeEnsinoVO> lista, String tipoConsulta, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, TurmaVO turmaVO, Boolean controlaAprovacaoDocEntregue) throws Exception {
        DebitoDocumentosAlunoRel.emitirRelatorio(getIdEntidade(), true, usuarioVO);
        if (debitoDocumentoAlunoRelVO.getMatriculaVO() != null && !debitoDocumentoAlunoRelVO.getMatriculaVO().getMatricula().equals("")) {
            matriculaperiodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoPorMatriculaAnoSemestre(debitoDocumentoAlunoRelVO.getMatriculaVO().getMatricula(), false,
                    Uteis.NIVELMONTARDADOS_DADOSBASICOS, configuracaoFinanceiroVO, usuarioVO);
        }
        List<DebitoDocumentosAlunoRelVO> listaDebitoDocumentosAlunoRelVO = new ArrayList<DebitoDocumentosAlunoRelVO>(0);
        SqlRowSet dadosSQL = executarConsultaParametrizada(preMatricula, debitoDocumentoAlunoRelVO, matriculaperiodoVO, dataInicio, dataFim,  usuarioVO, ano, semestre, filtrarCursoAnual, filtrarCursoSemestral, filtrarCursoIntegral,
                documentoEntregue, documentoPendente, documentoEntregaIndeferida, documentoPendenteAprovacao, lista, tipoConsulta, filtroRelatorioAcademicoVO, turmaVO, controlaAprovacaoDocEntregue);
        while (dadosSQL.next()) {
            listaDebitoDocumentosAlunoRelVO.add(montarDados(dadosSQL, controlaAprovacaoDocEntregue));
        }
        return listaDebitoDocumentosAlunoRelVO;
    }
    
    @Override
    public Map<String, List<DebitoDocumentosAlunoRelVO>> realizarGeracaoListaAlunoComDocumentacaoPendenteEnvioEmail() throws Exception {        
        Map<String, List<DebitoDocumentosAlunoRelVO>> listaAlunoDocumentacaoPendente = new HashMap<String, List<DebitoDocumentosAlunoRelVO>>();
        
        SqlRowSet dadosSQL = consultarAlunoComDocumentacaoPendenteEnvioEmail();        
        while (dadosSQL.next()) {
            DebitoDocumentosAlunoRelVO debitoDocumentosAlunoRelVO = new DebitoDocumentosAlunoRelVO();
            debitoDocumentosAlunoRelVO.getMatriculaVO().setMatricula(dadosSQL.getString("matricula_matricula"));
            debitoDocumentosAlunoRelVO.getMatriculaVO().getAluno().setCodigo(dadosSQL.getInt("pessoa_codigo"));
            debitoDocumentosAlunoRelVO.getMatriculaVO().getAluno().setNome(dadosSQL.getString("pessoa_nome"));
            debitoDocumentosAlunoRelVO.getMatriculaVO().getAluno().setEmail(dadosSQL.getString("pessoa_email"));
            debitoDocumentosAlunoRelVO.getMatriculaVO().getUnidadeEnsino().setNome(dadosSQL.getString("unidadeEnsino_nome"));
            debitoDocumentosAlunoRelVO.getMatriculaVO().getCurso().setNome(dadosSQL.getString("curso_nome"));            
            debitoDocumentosAlunoRelVO.setTipoDocumento(dadosSQL.getString("tipodocumento_nome"));            
            if(listaAlunoDocumentacaoPendente.containsKey(dadosSQL.getString("matricula_matricula"))){
                listaAlunoDocumentacaoPendente.get(dadosSQL.getString("matricula_matricula")).add(debitoDocumentosAlunoRelVO);
            }else{
                List<DebitoDocumentosAlunoRelVO> documentosAlunoRelVOs = new ArrayList<DebitoDocumentosAlunoRelVO>(0);
                documentosAlunoRelVOs.add(debitoDocumentosAlunoRelVO);
                listaAlunoDocumentacaoPendente.put(dadosSQL.getString("matricula_matricula"), documentosAlunoRelVOs);
            }
        }
        
        return listaAlunoDocumentacaoPendente;
        
    }
    
    public SqlRowSet consultarAlunoComDocumentacaoPendenteEnvioEmail() throws Exception {
        StringBuilder selectStr = new StringBuilder();

        selectStr.append(" SELECT ");        
        selectStr.append(" matricula.matricula AS matricula_matricula,");
        selectStr.append(" tipodocumento.nome AS tipodocumento_nome,");
        selectStr.append(" pessoa.nome AS pessoa_nome,");
        selectStr.append(" pessoa.codigo AS pessoa_codigo,");
        selectStr.append(" pessoa.email AS pessoa_email,");
        selectStr.append(" unidadeEnsino.nome AS unidadeEnsino_nome, ");
        selectStr.append(" curso.nome AS curso_nome ");
        selectStr.append(" FROM matricula");        
        selectStr.append(" INNER JOIN pessoa ON (matricula.aluno = pessoa.codigo)");
        selectStr.append(" INNER JOIN unidadeEnsino ON (matricula.unidadeEnsino = unidadeEnsino.codigo)");
        selectStr.append(" INNER JOIN configuracoes on   configuracoes.codigo = unidadeensino.configuracoes");
        selectStr.append(" INNER JOIN curso ON (matricula.curso = curso.codigo)");
        selectStr.append(" INNER JOIN documetacaomatricula ON (matricula.matricula = documetacaomatricula.matricula) ");
        selectStr.append(" INNER JOIN tipodocumento ON (documetacaomatricula.tipodedocumento = tipodocumento.codigo)");
        selectStr.append(" inner join matriculaperiodo on   matriculaperiodo.matricula = matricula.matricula and ");
        selectStr.append(" matriculaperiodo.codigo  = (select max(mp.codigo) from matriculaperiodo mp where mp.matricula = matricula.matricula ) ");
        selectStr.append(" where matricula.situacao =  'AT' and documetacaomatricula.entregue = false and documetacaomatricula.dataentrega is null ");
        selectStr.append(" and configuracoes.controlarNotificacaoPendenciaDocumentos = true ");
        selectStr.append(" and (( matricula.dataEnvioNotificacaoPendenciaDocumento is null and"); 
        selectStr.append(" (select min(data) from horarioturma"); 
        selectStr.append(" inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo");
        selectStr.append(" where horarioturma.turma = matriculaperiodo.turma) <= (current_date+nrDiaAposInicioAulaNotificarPendenciaDocumento)");
        selectStr.append(" and ( (configuracoes.controlarSuspensaoMatriculaPendenciaDocumentos and  (");
        selectStr.append(" matricula.dataenvionotificacao3 is not null or");
        selectStr.append(" matricula.matricula not in (select dm.matricula from documetacaomatricula dm where matricula.matricula = dm.matricula");
        selectStr.append(" and dm.entregue = false and dm.gerarsuspensaomatricula = true limit 1)");
        selectStr.append(" ))"); 
        selectStr.append(" or (configuracoes.controlarSuspensaoMatriculaPendenciaDocumentos = false))");                    
        selectStr.append(" ) or ( matricula.dataEnvioNotificacaoPendenciaDocumento+periodicidadeNotificarPendenciaDocumento<= current_date ))");
        selectStr.append(" order by matricula.matricula, tipodocumento.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(selectStr.toString());
        return tabelaResultado;
    }

    public DebitoDocumentosAlunoRelVO montarDados(SqlRowSet dadosSQL, Boolean controlaAprovacaoDocEntregue) throws Exception {
        DebitoDocumentosAlunoRelVO debitoDocumentosAlunoRelVO = new DebitoDocumentosAlunoRelVO();
        debitoDocumentosAlunoRelVO.getCursoVO().setNome(dadosSQL.getString("curso_nome"));
        debitoDocumentosAlunoRelVO.getMatriculaVO().setMatricula(dadosSQL.getString("matricula_matricula"));
        debitoDocumentosAlunoRelVO.getTurmaVO().setIdentificadorTurma(dadosSQL.getString("turma_identificadorturma"));
        debitoDocumentosAlunoRelVO.setTipoDocumento(dadosSQL.getString("tipodocumento_nome"));
        debitoDocumentosAlunoRelVO.getMatriculaVO().setData(dadosSQL.getDate("matricula_data"));
        debitoDocumentosAlunoRelVO.getTurmaVO().getUnidadeEnsino().setNome(dadosSQL.getString("unidadeEnsino_nome"));
        debitoDocumentosAlunoRelVO.getMatriculaVO().getAluno().setNome(dadosSQL.getString("pessoa_nome"));
        debitoDocumentosAlunoRelVO.getMatriculaVO().getAluno().setEmail(dadosSQL.getString("pessoa_email"));
        debitoDocumentosAlunoRelVO.getMatriculaVO().getAluno().setCelular(dadosSQL.getString("pessoa_celular"));
        debitoDocumentosAlunoRelVO.getMatriculaVO().getConsultor().getPessoa().setNome(dadosSQL.getString("p2_nome"));

        debitoDocumentosAlunoRelVO.setDataEntrega(Uteis.getDataJDBC(dadosSQL.getDate("documetacaomatricula_dataentrega")));
        debitoDocumentosAlunoRelVO.setNomeUsuario(dadosSQL.getString("usuario_nome"));

        debitoDocumentosAlunoRelVO.setJustificativaNegacao(dadosSQL.getString("justificativanegacao"));
        
         if(dadosSQL.getBoolean("entregue")) {
          	debitoDocumentosAlunoRelVO.setSituacaoDocumento("Entregue");
	        if(dadosSQL.getDate("documetacaomatricula_dataentrega") != null) {
	        	debitoDocumentosAlunoRelVO.setDataSituacaoDocumento(Uteis.getDataJDBC(dadosSQL.getDate("documetacaomatricula_dataentrega")));
	        }else if(dadosSQL.getDate("dataAprovacao") != null) {
	          	debitoDocumentosAlunoRelVO.setDataSituacaoDocumento(Uteis.getDataJDBC(dadosSQL.getDate("dataAprovacao")));
	        }
          	
         }       

         if(!dadosSQL.getBoolean("entregue")  && dadosSQL.getBoolean("pendenteAprovacao") && dadosSQL.getDate("dataAprovacao") == null && dadosSQL.getDate("dataIndeferimento") == null) {
          	debitoDocumentosAlunoRelVO.setSituacaoDocumento("Pendente Aprovação");
          	debitoDocumentosAlunoRelVO.setDataSituacaoDocumento(Uteis.getDataJDBC(dadosSQL.getDate("dataUpload")));
          }
          else if(!dadosSQL.getBoolean("entregue") && dadosSQL.getDate("dataAprovacao") == null && dadosSQL.getDate("dataIndeferimento") != null) {
          	debitoDocumentosAlunoRelVO.setSituacaoDocumento("Entrega Indeferida");
          	debitoDocumentosAlunoRelVO.setDataSituacaoDocumento(Uteis.getDataJDBC(dadosSQL.getDate("dataIndeferimento")));
          }
          else if(!dadosSQL.getBoolean("entregue") && dadosSQL.getDate("dataAprovacao") == null && dadosSQL.getDate("dataIndeferimento") == null){
        	  debitoDocumentosAlunoRelVO.setSituacaoDocumento("Pendente");
          }

          
        return debitoDocumentosAlunoRelVO;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * relatorio.negocio.jdbc.academico.DebitoDocumentosAlunoRelInterfaceFacade#executarConsultaParametrizada(java.lang
     * .Boolean)
     */
    public SqlRowSet executarConsultaParametrizada(Boolean preMatricula, DebitoDocumentosAlunoRelVO debitoDocumentosAlunoRelVO, MatriculaPeriodoVO matriculaperiodoVO, Date dataInicio, Date dataFim, UsuarioVO usuarioVO, String ano, String semestre, Boolean filtrarCursoAnual, Boolean filtrarCursoSemestral, Boolean filtrarCursoIntegral,
            Boolean documentoEntregue, Boolean documentoPendente, Boolean documentoEntregaIndeferida, Boolean documentoPendenteAprovacao, List<UnidadeEnsinoVO> lista, String tipoConsulta, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, TurmaVO turmaVO, Boolean controlaAprovacaoDocEntregue) throws Exception {
        StringBuilder selectStr = new StringBuilder();

        selectStr.append("SELECT");
        selectStr.append(" curso.nome AS curso_nome,");
        selectStr.append(" matricula.matricula AS matricula_matricula,");
        selectStr.append(" matricula.data AS matricula_data,");
        selectStr.append(" turma.identificadorturma AS turma_identificadorturma,");
        selectStr.append(" tipodocumento.nome AS tipodocumento_nome,");
        selectStr.append(" pessoa.nome AS pessoa_nome, ");
        selectStr.append(" pessoa.email AS pessoa_email,");
        selectStr.append(" pessoa.celular AS pessoa_celular,");
        selectStr.append(" unidadeEnsino.nome AS unidadeEnsino_nome,");
        selectStr.append(" p2.nome AS p2_nome,");
        selectStr.append(" documetacaomatricula.dataentrega AS documetacaomatricula_dataentrega,");          
        selectStr.append(" usuario.nome AS usuario_nome, ");
        selectStr.append(" documetacaomatricula.entregue as entregue,"); 
        selectStr.append(" documetacaomatricula.datanegardocdep as dataIndeferimento,");    
        selectStr.append(" documetacaomatricula.justificativanegacao,"); 
        selectStr.append(" arquivo.dataupload,");
        selectStr.append(" documetacaomatricula.dataaprovacaodocdep as dataAprovacao,");
        selectStr.append(" case when (select codigo from arquivo where arquivo.codigo=documetacaomatricula.arquivo \n" + 
        		"and documetacaomatricula.arquivoAprovadoPeloDep = false \n" + 
        		"and (documetacaomatricula.respnegardocdep is null or documetacaomatricula.respnegardocdep = 0) limit 1) is not null then true else false\n" + 
        		"end AS pendenteAprovacao ");
        selectStr.append(" FROM matricula");
        selectStr.append(" INNER JOIN curso ON (matricula.curso = curso.codigo)");
        selectStr.append(" INNER JOIN matriculaperiodo ON (matricula.matricula = matriculaperiodo.matricula)");
        selectStr.append(" INNER JOIN turma ON (matriculaperiodo.turma = turma.codigo)");
        selectStr.append(" INNER JOIN unidadeEnsino ON (matricula.unidadeEnsino = unidadeEnsino.codigo)");
        selectStr.append(" LEFT JOIN funcionario ON (matricula.consultor = funcionario.codigo)");
        selectStr.append(" LEFT JOIN pessoa p2 ON (p2.codigo = funcionario.pessoa)");
        selectStr.append(" INNER JOIN pessoa ON (matricula.aluno = pessoa.codigo)");
        selectStr.append(" INNER JOIN documetacaomatricula ON (matricula.matricula = documetacaomatricula.matricula)");
        selectStr.append(" LEFT JOIN usuario ON (usuario.codigo = documetacaomatricula.usuario)");
        selectStr.append(" INNER JOIN tipodocumento ON (documetacaomatricula.tipodedocumento = tipodocumento.codigo)");
        selectStr.append(" LEFT join arquivo on documetacaomatricula.arquivo=arquivo.codigo ");
        selectStr.append(montarFiltrosRelatorio(preMatricula, debitoDocumentosAlunoRelVO, matriculaperiodoVO, dataInicio, dataFim, usuarioVO, ano, semestre, filtrarCursoAnual, filtrarCursoSemestral, filtrarCursoIntegral, documentoEntregue, documentoPendente, documentoEntregaIndeferida, documentoPendenteAprovacao, tipoConsulta, filtroRelatorioAcademicoVO, turmaVO, controlaAprovacaoDocEntregue));

        if (!lista.isEmpty()) {
        	selectStr.append(" and unidadeensino.codigo in (");
			int x = 0;
			for (UnidadeEnsinoVO unidadeEnsinoVO : lista) {
				if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
					if (x > 0) {
						selectStr.append(", ");
					}
					selectStr.append(unidadeEnsinoVO.getCodigo());
					x++;
				}

			}
			selectStr.append(" ) ");
		}
        selectStr.append(" GROUP BY curso_nome, matricula_matricula, matricula_data, turma_identificadorturma, tipodocumento_nome, pessoa_nome, pessoa_email, pessoa_celular, unidadeEnsino_nome, p2_nome,  documetacaomatricula.dataentrega, documetacaomatricula.entregue, documetacaomatricula.datanegardocdep, documetacaomatricula.arquivo, documetacaomatricula.arquivoAprovadoPeloDep, \n" + 
        		"documetacaomatricula.respnegardocdep, documetacaomatricula.justificativanegacao, arquivo.dataupload, documetacaomatricula.dataaprovacaodocdep, documetacaomatricula_dataentrega, usuario_nome ");
        
        selectStr.append(montarOrdenacaoRelatorio());
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(selectStr.toString());
        return tabelaResultado;
    }  
   
    private String montarFiltrosRelatorio(Boolean preMatricula, DebitoDocumentosAlunoRelVO debitoDocumentosAlunoRelVO, MatriculaPeriodoVO matriculaperiodoVO, Date dataInicio, Date dataFim, UsuarioVO usuarioVO, String ano, String semestre, Boolean filtrarCursoAnual, Boolean filtrarCursoSemestral, Boolean filtrarCursoIntegral, Boolean documentoEntregue, Boolean documentoPendente, Boolean documentoEntregaIndeferida, Boolean documentoPendenteAprovacao, String tipoConsulta, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, TurmaVO turmaVO, Boolean controlaAprovacaoDocEntregue) throws Exception {
        String condicao = " where 1=1 ";
        //String condicao = "";
       /* if(!controle) {
        	condicao += where + "documetacaomatricula.situacao in('PE', 'OK')";
        } else {
            condicao += where + "documetacaomatricula.situacao = 'OK'";
        }
        where = " and ";*/
        
        if (tipoConsulta.equals("turma") || tipoConsulta.equals("curso")) {
        	condicao += " and " + adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo");
        }
        
        if(filtroRelatorioAcademicoVO.getConfirmado() != false || filtroRelatorioAcademicoVO.getPendenteFinanceiro() != false) {
        	condicao += " and " + adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo");
        }
        
        if (Uteis.isAtributoPreenchido(debitoDocumentosAlunoRelVO.getCursoVO())) {
            condicao += " and curso.codigo = " + debitoDocumentosAlunoRelVO.getCursoVO().getCodigo() + " AND turma.curso = curso.codigo";
           
        }
        if (Uteis.isAtributoPreenchido(turmaVO)) {
            condicao += " and turma.codigo = " + turmaVO.getCodigo();
           
        }
        if (Uteis.isAtributoPreenchido(debitoDocumentosAlunoRelVO.getTipoDocumentoVO())) {
            condicao += " and tipodocumento.codigo = " + debitoDocumentosAlunoRelVO.getTipoDocumentoVO().getCodigo();

        }
        if (Uteis.isAtributoPreenchido(debitoDocumentosAlunoRelVO.getFuncionarioVO())) {
            condicao += " and funcionario.codigo = " + debitoDocumentosAlunoRelVO.getFuncionarioVO().getCodigo();

        }
        if (Uteis.isAtributoPreenchido(debitoDocumentosAlunoRelVO.getUnidadeEnsinoVO())) {
            condicao += " and unidadeEnsino.codigo = " + debitoDocumentosAlunoRelVO.getUnidadeEnsinoVO().getCodigo();

        }
        if (Uteis.isAtributoPreenchido(usuarioVO.getUnidadeEnsinoLogado())) {
            condicao += " and matricula.unidadeensino = " + usuarioVO.getUnidadeEnsinoLogado().getCodigo();

        }
        if (Uteis.isAtributoPreenchido(debitoDocumentosAlunoRelVO.getTurmaVO())) {
            condicao += " and turma.codigo = " + debitoDocumentosAlunoRelVO.getTurmaVO().getCodigo();
        }
        if(filtrarCursoIntegral) {
	        if (Uteis.isAtributoPreenchido(dataInicio)) {
	            condicao += " and matricula.data >= '" + Uteis.getDataJDBC(dataInicio) + "'";
	        }
	        if (Uteis.isAtributoPreenchido(dataFim)) {
	            condicao += " and matricula.data <= '" + Uteis.getDataJDBC(dataFim) + "'";
	        }
        }
        if(filtrarCursoAnual && !filtrarCursoSemestral) {
        	if(!ano.trim().isEmpty()){
        		condicao += " and curso.periodicidade = 'AN' and matriculaPeriodo.ano = '" + ano + "'";
        	}
        }
        else if(filtrarCursoSemestral) {
        	if(!semestre.trim().isEmpty()){
        		condicao += " and curso.periodicidade = 'SE' and matriculaPeriodo.ano = '" + ano + "' and matriculaPeriodo.semestre = '" + semestre + "'";
        	}
        }
        if (matriculaperiodoVO.getCodigo() != null && matriculaperiodoVO.getCodigo() > 0) {
            condicao += "and  matriculaPeriodo.codigo = " + matriculaperiodoVO.getCodigo();
        }

//        if (preMatricula) {
//        	condicao += " and matriculaPeriodo.situacaomatriculaperiodo in ('AT', 'CO', 'FI', 'FO', 'PR')";
//        } else {
//        	condicao += " and matriculaPeriodo.situacaomatriculaperiodo in ('AT', 'CO', 'FI', 'FO')";
//        }    
        
        
        String andOr = " and ( ";
        if(documentoEntregue) {
        	condicao += andOr + " documetacaomatricula.entregue is true";
        	andOr = " or ";
        }    
       
        if(documentoPendente) {
        	condicao += andOr + "(documetacaomatricula.entregue is false and documetacaomatricula.dataaprovacaodocdep is null and documetacaomatricula.datanegardocdep is null" + 
        			" and (select codigo from arquivo where arquivo.codigo=documetacaomatricula.arquivo" + 
        			" and documetacaomatricula.arquivoAprovadoPeloDep = false" + 
        			" and (documetacaomatricula.respnegardocdep is null or documetacaomatricula.respnegardocdep = 0) limit 1) is null) ";
        	andOr = " or ";
        }
        
        if(documentoEntregaIndeferida) {
        	condicao += andOr + "(documetacaomatricula.entregue is false and documetacaomatricula.dataaprovacaodocdep is null and documetacaomatricula.datanegardocdep is not null" + 
        			" and (select codigo from arquivo where arquivo.codigo=documetacaomatricula.arquivo" + 
        			" and documetacaomatricula.arquivoAprovadoPeloDep = false" + 
        			" and (documetacaomatricula.respnegardocdep is null or documetacaomatricula.respnegardocdep = 0) limit 1) is null) ";
        	andOr = " or ";
        }
        
        if(documentoPendenteAprovacao) {
        	condicao += andOr + "(documetacaomatricula.entregue is false  and documetacaomatricula.dataaprovacaodocdep is null and documetacaomatricula.datanegardocdep is null" + 
        			" and (select codigo from arquivo where arquivo.codigo=documetacaomatricula.arquivo" + 
        			" and documetacaomatricula.arquivoAprovadoPeloDep = false" + 
        			" and (documetacaomatricula.respnegardocdep is null or documetacaomatricula.respnegardocdep = 0) limit 1) is not null) ";
        	andOr = " or ";
        }
        
        if(documentoEntregue || documentoPendente || documentoEntregaIndeferida || documentoPendenteAprovacao) {
        	condicao += ")";
        }
                        
        return condicao;
    }

    /**
     * Mï¿½todo responsï¿½vel por definir os filtros dos dados a serem apresentados no relatï¿½rio.
     *
     * @param selectStr
     *            consulta inicialmente preparada, para a qual os filtros serï¿½o adicionados.
     */
    protected String montarOrdenacaoRelatorio() {
        String ordenacao = (String) getOrdenacoesRelatorio().get(getOrdenarPor());
        String condicao = "";

        if (ordenacao.equals("Aluno")) {
            ordenacao = "pessoa.nome";
        }
        if (ordenacao.equals("Turma")) {
            ordenacao = "turma.identificadorturma";
        }
        if (ordenacao.equals("Tipo Documento")) {
            ordenacao = "tipoDocumento.nome";
        }
        if (ordenacao.equals("Unidade Ensino")) {
            ordenacao = "unidadeEnsino.nome";
        }
        if (ordenacao.equals("Consultor")) {
            ordenacao = "p2.nome ";
        }
        if (!ordenacao.equals("")) {
            condicao = condicao + " ORDER BY " + ordenacao;
        }
        return condicao;

    }

    /**
     * Operaï¿½ï¿½o reponsï¿½vel por retornar o arquivo (caminho e nome) correspondente ao design do relatï¿½rio criado
     * pelo IReport.
     */
    public static String getDesignIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "DebitoDocumentosAlunoRel" + ".jrxml");
    }

    public static String getCaminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico");
    }

    public static String getIdEntidade() {
        return ("ControleDocumentacaoAlunoRel");
    }

    /*
     * (non-Javadoc)
     *
     * @see relatorio.negocio.jdbc.academico.DebitoDocumentosAlunoRelInterfaceFacade#inicializarOrdenacoesRelatorio()
     */
    public void inicializarOrdenacoesRelatorio() {
        Vector ordenacao = this.getOrdenacoesRelatorio();
        ordenacao.add("Aluno");
        ordenacao.add("Turma");
        ordenacao.add("Unidade Ensino");
        ordenacao.add("Tipo Documento");
        ordenacao.add("Consultor");
    }

    public static void validarDados(String tipoConsulta, DebitoDocumentosAlunoRelVO debitoDocumentoAlunoRelVO, Boolean filtrarCursoAnual, Boolean filtrarCursoSemestral, Boolean filtrarCursoIntegral, String ano, String semestre, Date dataInicio, Date dataFim, TurmaVO turmaVO) throws Exception {
    	if (tipoConsulta.equals("aluno")) {
    		if(debitoDocumentoAlunoRelVO.getMatriculaVO().getMatricula() == null 
    				|| debitoDocumentoAlunoRelVO.getMatriculaVO().getMatricula().equals("") 
    					|| debitoDocumentoAlunoRelVO.getMatriculaVO().getAluno().getNome().equals("")) {
    			throw new Exception("O Aluno deve ser informado para geração do relatório.");
    		}
    	} 
    	else if (tipoConsulta.equals("curso")) {
//    		if(debitoDocumentoAlunoRelVO.getUnidadeEnsinoVO().getCodigo() == null
//    				|| debitoDocumentoAlunoRelVO.getUnidadeEnsinoVO().getCodigo().intValue() == 0) {
//    			throw new Exception("A unidade de ensino deve ser informado para geração do relatório.");
//    		}
    		if(filtrarCursoSemestral && (semestre.equals("") || ano.equals("")) ) {
    			throw new Exception("O Semestre e o Ano devem ser informados.");
    		}
    		if(filtrarCursoAnual && ano.equals("")) {
    			throw new Exception("O Ano deve ser informado.");
    		}
    		if(filtrarCursoIntegral && (dataInicio == null || dataFim == null)){
    			throw new Exception("O período deve ser informado.");
    		}
    	}
    	else if(tipoConsulta.equals("turma")) {
    		if(turmaVO.getCodigo() == null || turmaVO.getCodigo() == 0) {
    			throw new Exception("A turma deve ser informada.");
    		}
    	}
    }

}
