package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.PlanoEnsinoVO;
import negocio.comuns.academico.ProgramacaoFormaturaAlunoVO;
import negocio.comuns.academico.ProgramacaoFormaturaVO;

import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.TipoDocumentoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.academico.enumeradores.TipoDoTextoImpressaoContratoEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;

import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Extenso;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.Escolaridade;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplina;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.negocio.comuns.academico.CertificadoCursoExtensaoDisciplinasRelVO;
import relatorio.negocio.comuns.academico.CertificadoCursoExtensaoRelVO;
import relatorio.negocio.interfaces.academico.CertificadoCursoExtensaoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class CertificadoCursoExtensaoRel extends SuperRelatorio implements CertificadoCursoExtensaoRelInterfaceFacade {

	public static void validarDados(MatriculaVO matriculaVO, String filtro , String tipoLayout, TurmaVO turmaVO, RequerimentoVO requerimento, CertificadoCursoExtensaoRelVO certificadoCursoExtensaoRelVO , UnidadeEnsinoVO unidadeEnsinoVO , ProgramacaoFormaturaVO programacaoFormatura ) throws ConsistirException {
        if (filtro.equals("aluno") && matriculaVO.getMatricula().equals("")) {
            throw new ConsistirException("O Aluno deve ser informado para geração do relatório.");
        }
        if (filtro.equals("turma") && (turmaVO.getCodigo() == null || turmaVO.getCodigo().intValue() == 0)) {
            throw new ConsistirException("O Curso e a Turma devem ser informados para geração do relatório.");
        }
        if (filtro.equals("requerimento") && (requerimento.getCodigo() == null || requerimento.getCodigo().intValue() == 0)) {
        	throw new ConsistirException("O Requerimento devem ser informados para geração do relatório.");
        }
        if (filtro.equals("programacaoFormatura") && (programacaoFormatura.getCodigo() == null  ||  programacaoFormatura.getCodigo().intValue() == 0)){
        	throw new ConsistirException("A Programação de Formatura devem ser informados para geração do relatório.");
        }
        if(!Uteis.isAtributoPreenchido(unidadeEnsinoVO)){
        	throw new ConsistirException("A Unidade Ensino devem ser informados para geração do relatório.");
        }
        if(!Uteis.isAtributoPreenchido(tipoLayout)){
        	throw new ConsistirException("O Tipo de Layout devem ser informados para geração do relatório.");
        }
        if(certificadoCursoExtensaoRelVO.getFuncionarioPrincipalVO().getCodigo()==null || certificadoCursoExtensaoRelVO.getFuncionarioPrincipalVO().getCodigo() == 0){
            throw new ConsistirException("O Funcionario 1 deve ser informado para geração do relatório.");
        }
        if(certificadoCursoExtensaoRelVO.getFuncionarioSecundarioVO().getCodigo()==null || certificadoCursoExtensaoRelVO.getFuncionarioSecundarioVO().getCodigo() == 0){
            throw new ConsistirException("O Funcionario 2 deve ser informado para geração do relatório.");
        }
        if(certificadoCursoExtensaoRelVO.getCargoFuncionarioPrincipal().getCodigo()==null || certificadoCursoExtensaoRelVO.getCargoFuncionarioPrincipal().getCodigo() == 0){
            throw new ConsistirException("O Cargo do Funcionario 1 deve ser informado para geração do relatório.");
        }
        if(certificadoCursoExtensaoRelVO.getCargoFuncionarioSecundario().getCodigo()==null || certificadoCursoExtensaoRelVO.getCargoFuncionarioSecundario().getCodigo() == 0){
            throw new ConsistirException("O Cargo do Funcionario 2 deve ser informado para geração do relatório.");
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * relatorio.negocio.jdbc.academico.HistoricoAlunoRelInterfaceFacade#criarObjeto(relatorio.negocio.comuns.academico
     * .HistoricoAlunoRelVO, negocio.comuns.academico.MatriculaVO, negocio.comuns.academico.GradeCurricularVO, int, int)
     */
    public List<CertificadoCursoExtensaoRelVO> criarObjeto(CertificadoCursoExtensaoRelVO certificadoCursoExtensaoRelVO, MatriculaVO matriculaVO, PeriodoLetivoVO periodoLetivoVO, TurmaVO turmaVO, String tipoLayout, String filtro,  boolean trazerApenasDisciplinaPeriodoSelecionado, DisciplinaVO disciplinaVO, UsuarioVO usuarioVO, Boolean trazerTodasSituacoesAprovadas , ProgramacaoFormaturaVO programacaoFormaturaVO , List<CertificadoCursoExtensaoRelVO> listaCertificadoErro , List<CertificadoCursoExtensaoRelVO> listaCertificadoCursoExtensaoRelVOGerar) throws Exception {
        List<CertificadoCursoExtensaoRelVO> lista = new ArrayList<CertificadoCursoExtensaoRelVO>(0);
        List<MatriculaVO> listaMatricula = new ArrayList<MatriculaVO>(0);
        montarDadosFuncionarios(certificadoCursoExtensaoRelVO, usuarioVO);
       /* if (filtro.equals("turma")) {
        	getFacadeFactory().getTurmaFacade().carregarDados(turmaVO, NivelMontarDados.BASICO, usuarioVO);
        	listaMatricula = getFacadeFactory().getMatriculaFacade().executarConsultaPorTurmaCurso(turmaVO.getCodigo(), turmaVO.getCurso().getCodigo(), turmaVO.getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);
        	periodoLetivoVO = turmaVO.getPeridoLetivo();
        } else*/
        
	    	if(filtro.equals("programacaoFormatura") ||  filtro.equals("turma")){ 
	    	certificadoCursoExtensaoRelVO.setUnidadeEnsinoVO(new UnidadeEnsinoVO());
	    	if(Uteis.isAtributoPreenchido(listaCertificadoCursoExtensaoRelVOGerar)  &&  !listaCertificadoCursoExtensaoRelVOGerar.isEmpty()) {        		
	    		listaMatricula	=  listaCertificadoCursoExtensaoRelVOGerar.stream().map(CertificadoCursoExtensaoRelVO::getMatriculaVO).collect(Collectors.toList());
	    	}
        }else {
            listaMatricula.add(matriculaVO);
        }
        if(!listaMatricula.isEmpty()){
        	if (tipoLayout.equals("CertificadoCursoExtensaoRel")) {
            	lista = executarConsultaParametrizada(listaMatricula, certificadoCursoExtensaoRelVO, tipoLayout);
            	montarCargaHorariaPorExtenso(lista);
            } else {
            	if(filtro.equals("programacaoFormatura")) {        
            		certificadoCursoExtensaoRelVO.setUnidadeEnsinoVO(programacaoFormaturaVO.getUnidadeEnsino());
            	}else {
            		validarDadosPeriodoLetivoEmissaoCertificado(periodoLetivoVO.getCodigo());
            		certificadoCursoExtensaoRelVO.setUnidadeEnsinoVO(listaMatricula.get(0).getUnidadeEnsino());
            		certificadoCursoExtensaoRelVO.setPeriodoLetivoVO(getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(periodoLetivoVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO));
            		
            	}
            	lista = executarConsultaParametrizadaLayout2(listaMatricula, certificadoCursoExtensaoRelVO, tipoLayout, trazerApenasDisciplinaPeriodoSelecionado, disciplinaVO, "AP", trazerTodasSituacoesAprovadas);
            	validarRegraExistenciaHistoricoEmMatriculasVindoProgramacaoFormatura(lista,listaCertificadoErro ,listaMatricula);
            }	
        }
        return lista;
    }

    @Override
    public List<CertificadoCursoExtensaoRelVO> executarConsultaParametrizadaLayout2(List<MatriculaVO> listaMatricula, CertificadoCursoExtensaoRelVO certificadoCursoExtensaoRelVO, String tipoLayout, boolean trazerApenasDisciplinaPeriodoSelecionado, DisciplinaVO disciplinaVO, String situacaoHistorico, Boolean trazerTodasSituacoesAprovadas) throws Exception {
    	String separador = "";  
    	StringBuilder sqlStr = new StringBuilder("");
    	sqlStr.append(" SELECT DISTINCT pessoa.nome AS pessoanome, pessoa.rg as rg, pessoa.orgaoemissor, pessoa.estadoemissaorg, curso.nome AS cursonome, curso.nrregistrointerno, ");
    	sqlStr.append(" periodoletivo.nomecertificacao, periodoletivo.descricao AS periodoletivo_descricao, ");
    	sqlStr.append(" matriculaperiodo.matricula, unidadeensino.nomeexpedicaodiploma AS nomeexpedicaodiploma, unidadeensino.nome AS unidadeensinonome, ");
    	sqlStr.append(" cidade.nome AS cidadenome, estado.sigla AS estadosigla, matriculaperiodo.ano, matriculaperiodo.semestre, disciplina.nome AS disciplina, gradedisciplina.cargahoraria as disciplina_cargahoraria, historico.mediafinal, historico.situacao, ");
    	sqlStr.append(" gradedisciplina.tipodisciplina, historico.freguencia, professor.nome as professortitular, formacaoacademica.escolaridade, areaconhecimento.nome as areaconhecimento, matricula.nomemonografia, matricula.notamonografia, ");
    	sqlStr.append("quantidadeCasasDecimaisPermitirAposVirgula, apresentarSiglaConcessaoCredito, utilizaNotaFinalConceito, notaFinalConceito");
    	sqlStr.append(" from matricula ");
    	sqlStr.append(" INNER JOIN matriculaperiodo ON matricula.matricula = matriculaperiodo.matricula ");
    	sqlStr.append(" INNER JOIN unidadeensino ON unidadeensino.codigo = matricula.unidadeensino ");
    	sqlStr.append(" INNER JOIN cidade ON cidade.codigo = unidadeensino.cidade ");
    	sqlStr.append(" INNER JOIN estado ON estado.codigo = cidade.estado ");
    	sqlStr.append(" INNER JOIN pessoa ON pessoa.codigo = matricula.aluno ");
    	sqlStr.append(" INNER JOIN gradecurricular ON gradecurricular.codigo = matricula.gradecurricularatual ");
    	sqlStr.append(" INNER JOIN historico ON historico.matricula = matricula.matricula and historico.matriculaperiodo = matriculaperiodo.codigo and historico.matrizcurricular = gradecurricular.codigo ");
    	sqlStr.append(" INNER JOIN periodoletivo ON periodoletivo.codigo = historico.periodoletivomatrizcurricular ");
    	sqlStr.append(" INNER JOIN curso ON curso.codigo = matricula.curso ");
    	sqlStr.append(" inner join configuracaoacademico on configuracaoacademico.codigo = historico.configuracaoacademico ");
    	sqlStr.append(" INNER JOIN disciplina ON disciplina.codigo = historico.disciplina ");
    	sqlStr.append(" LEFT JOIN gradedisciplina on historico.gradedisciplina = gradedisciplina.codigo ");
    	sqlStr.append(" left JOIN matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina ");
    	sqlStr.append(" LEFT JOIN professortitulardisciplinaturma on professortitulardisciplinaturma.disciplina = matriculaperiodoturmadisciplina.disciplina ");
    	sqlStr.append(" and (professortitulardisciplinaturma.turma = matriculaperiodoturmadisciplina.turma or professortitulardisciplinaturma.turma in (select turmaorigem from turmaagrupada where turmaagrupada.turma = matriculaperiodoturmadisciplina.turma )) ");
    	sqlStr.append(" and case when curso.periodicidade = 'AN' then professortitulardisciplinaturma.ano = matriculaperiodo.ano else ");
    	sqlStr.append(" case when curso.periodicidade = 'SE' then  professortitulardisciplinaturma.ano = matriculaperiodo.ano and  professortitulardisciplinaturma.semestre = matriculaperiodo.semestre else true end end ");
    	sqlStr.append(" LEFT JOIN pessoa professor on professor.codigo = professortitulardisciplinaturma.professor ");
    	sqlStr.append(" LEFT JOIN formacaoacademica on formacaoacademica.pessoa = professor.codigo and formacaoacademica.codigo = (select fa.codigo from formacaoacademica fa ");
    	sqlStr.append(" where fa.pessoa = professor.codigo and fa.datafim <= matriculaperiodo.data order by fa.datafim desc limit 1) ");
    	sqlStr.append(" LEFT JOIN areaconhecimento on areaconhecimento.codigo = formacaoacademica.areaconhecimento ");
    	sqlStr.append(" WHERE matriculaperiodo.matricula in(");
        for (MatriculaVO matricula : listaMatricula) {
        	sqlStr.append(separador).append("'").append(matricula.getMatricula()).append("'");
            separador = ",";
        }
        sqlStr.append(") ");
        
        if (!certificadoCursoExtensaoRelVO.getPeriodoLetivoVO().getPeriodoLetivo().equals(0)) {        	
        	sqlStr.append(" AND periodoletivo.periodoletivo ").append(trazerApenasDisciplinaPeriodoSelecionado?" = ": " <= ").append(certificadoCursoExtensaoRelVO.getPeriodoLetivoVO().getPeriodoLetivo());
        }       
        MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and ");
        if(Uteis.isAtributoPreenchido(disciplinaVO)){
        	sqlStr.append(" AND historico.disciplina = ").append(disciplinaVO.getCodigo());
        }
        sqlStr.append(" and (historico.gradedisciplina is not null or historico.gradecurriculargrupooptativadisciplina is not null or historico.historicodisciplinaforagrade) ");
        sqlStr.append(" and (historico.historicoEquivalente = false or historico.historicoEquivalente is null) ");
        sqlStr.append(" and (historico.historicoDisciplinaFazParteComposicao = false or historico.historicoDisciplinaFazParteComposicao is null) ");
        if (trazerTodasSituacoesAprovadas) {
        	sqlStr.append(" and historico.situacao in ('AP', 'AA', 'AE', 'IS', 'CC', 'CH')");
        } else if (situacaoHistorico != null && !situacaoHistorico.equals("")) {
        	sqlStr.append(" and historico.situacao = 'AP'");
        }
        sqlStr.append(" ORDER BY pessoa.nome ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaLayout2(tabelaResultado, certificadoCursoExtensaoRelVO);
    }
    
    public List<CertificadoCursoExtensaoRelVO> montarDadosConsultaLayout2(SqlRowSet dadosSQL, CertificadoCursoExtensaoRelVO certificadoCursoExtensaoRelVO) throws Exception {
    	 List<CertificadoCursoExtensaoRelVO> vetResultado = new ArrayList<CertificadoCursoExtensaoRelVO>(0);
         while (dadosSQL.next()) {
        	 CertificadoCursoExtensaoRelVO obj = new CertificadoCursoExtensaoRelVO();
             obj.setFuncionarioPrincipalVO(certificadoCursoExtensaoRelVO.getFuncionarioPrincipalVO());
             obj.setFuncionarioSecundarioVO(certificadoCursoExtensaoRelVO.getFuncionarioSecundarioVO());
             obj.setCargoFuncionarioPrincipal(certificadoCursoExtensaoRelVO.getCargoFuncionarioPrincipal());
             obj.setCargoFuncionarioSecundario(certificadoCursoExtensaoRelVO.getCargoFuncionarioSecundario());  
             obj.setPeriodoLetivoVO(certificadoCursoExtensaoRelVO.getPeriodoLetivoVO());
             obj.setUnidadeEnsinoVO(certificadoCursoExtensaoRelVO.getUnidadeEnsinoVO());
        	 montarDadosLayout2(dadosSQL, vetResultado, obj);
        	 vetResultado.add(obj);
         }
         return vetResultado;
    }
    
    public void montarDadosLayout2(SqlRowSet dadosSQL, List<CertificadoCursoExtensaoRelVO> vetResultado, CertificadoCursoExtensaoRelVO obj) throws Exception {
        obj.setNomeAluno(dadosSQL.getString("pessoanome"));
        obj.setNomeCurso(dadosSQL.getString("cursonome"));
        obj.setRgAluno(dadosSQL.getString("rg"));
        obj.setOrgaoEmissorRgAluno(dadosSQL.getString("orgaoemissor"));
        obj.setEstadoEmissorRgAluno(dadosSQL.getString("estadoemissaorg"));
        obj.setMatricula(dadosSQL.getString("matricula"));
        obj.setMatriculaVO(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), obj.getUnidadeEnsinoVO().getCodigo(), NivelMontarDados.TODOS, new UsuarioVO()));
//        obj.setCargaHoraria(dadosSQL.getString("cargahoraria"));
        obj.setMantida(dadosSQL.getString("nomeexpedicaodiploma"));
        obj.setCredenciamento(dadosSQL.getString("nrregistrointerno"));
        obj.setMantida(dadosSQL.getString("unidadeensinonome"));
//    	obj.setDataAtualPorExtenso(dadosSQL.getString("cidadenome") + "/" + dadosSQL.getString("estadosigla"));
    	obj.setDataAtualPorExtenso(Uteis.getDataCidadeDiaMesPorExtensoEAno(dadosSQL.getString("cidadenome") + "/" + dadosSQL.getString("estadosigla"), new Date(),false));
    	obj.setCidadeDataAtual(Uteis.getDataCidadeDiaMesPorExtensoEAno(dadosSQL.getString("cidadenome"), new Date(), false));
    	obj.setNomeCertificacao(obj.getPeriodoLetivoVO().getNomeCertificacao());
    	if (obj.getNomeCertificacao().equals("")) {
    		obj.setNomeCertificacao(obj.getPeriodoLetivoVO().getDescricao());
    	}
    	 
    	CertificadoCursoExtensaoDisciplinasRelVO disciplina = null;
		String matricula = "";
		do {
			if ((matricula != null) && (!matricula.equals("")) && (!matricula.equals(dadosSQL.getString("matricula")))) {
				dadosSQL.previous();
				break;
			}
			disciplina = new CertificadoCursoExtensaoDisciplinasRelVO();
			disciplina.setNome(dadosSQL.getString("disciplina"));
			disciplina.setAno(dadosSQL.getString("ano"));
			disciplina.setSemestre(dadosSQL.getString("semestre"));
			disciplina.setCargaHoraria(dadosSQL.getString("disciplina_cargahoraria"));
			if (dadosSQL.getBoolean("apresentarSiglaConcessaoCredito") && dadosSQL.getString("situacao").equals("CC")) {
				disciplina.setMedia("CC");
			} else if (dadosSQL.getBoolean("utilizaNotaFinalConceito")) {
				disciplina.setMedia(dadosSQL.getString("notaFinalConceito"));
			} else {
				if (Uteis.isAtributoPreenchido(dadosSQL.getObject("mediafinal"))) {
					disciplina.setMedia(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(dadosSQL.getDouble("mediafinal"), dadosSQL.getInt("quantidadeCasasDecimaisPermitirAposVirgula")));
				}
			}
			disciplina.setSituacaoFinal(SituacaoHistorico.getDescricao(dadosSQL.getString("situacao")));
			disciplina.setTipoDisciplina(dadosSQL.getString("tipodisciplina"));
			disciplina.setMatricula(obj.getMatricula());
			disciplina.setFrequencia(dadosSQL.getString("freguencia"));
			disciplina.setNomeProfessor(dadosSQL.getString("professortitular"));
			if (Escolaridade.getDescricao(dadosSQL.getString("escolaridade")) != null && !Escolaridade.getDescricao(dadosSQL.getString("escolaridade")).equals("")) {
				disciplina.setTitulacaoProfessor(Escolaridade.getDescricao(dadosSQL.getString("escolaridade")) + " em " + dadosSQL.getString("areaconhecimento"));
			}
			disciplina.setNomeMonografia(dadosSQL.getString("nomemonografia"));
			disciplina.setNotaMonografia(dadosSQL.getString("notamonografia"));
			if (obj.getCargaHoraria() == null || obj.getCargaHoraria().equals("")) {
				obj.setCargaHoraria("0");
			}
			if (disciplina.getCargaHoraria() == null || disciplina.getCargaHoraria().equals("")) {
				disciplina.setCargaHoraria("0");
			}
			obj.setCargaHoraria(String.valueOf(Integer.parseInt(obj.getCargaHoraria()) + Integer.parseInt(disciplina.getCargaHoraria())));
			
			matricula = obj.getMatricula();
			obj.getCertificadoCursoExtensaoDisciplinasRelVOs().add(disciplina);
			if (dadosSQL.isLast()) {
				return;
			}
		} while (dadosSQL.next());
    	
    }
    
    public List<CertificadoCursoExtensaoRelVO> executarValidarSituacaoDisciplinaAprovada(List<CertificadoCursoExtensaoRelVO> certificados) throws Exception {
    	List<CertificadoCursoExtensaoRelVO> listaErro = new ArrayList<CertificadoCursoExtensaoRelVO>(0);
    	for (CertificadoCursoExtensaoRelVO certificado : certificados) {
    		Integer cargaHorariaOptativa = 0;
			for (CertificadoCursoExtensaoDisciplinasRelVO disciplina : certificado.getCertificadoCursoExtensaoDisciplinasRelVOs()) {
				if (disciplina.getTipoDisciplina().equals("OB")) {
					if (!disciplina.getSituacaoFinal().trim().equals(SituacaoHistorico.APROVADO.getDescricao().trim()) 
							&& !disciplina.getSituacaoFinal().trim().equals(SituacaoHistorico.APROVADO_POR_EQUIVALENCIA.getDescricao().trim()) 
							&& !disciplina.getSituacaoFinal().trim().equals(SituacaoHistorico.APROVADO_COM_DEPENDENCIA.getDescricao().trim()) 
							&& !disciplina.getSituacaoFinal().trim().equals(SituacaoHistorico.APROVADO_APROVEITAMENTO.getDescricao().trim()) 
							&& !disciplina.getSituacaoFinal().trim().equals(SituacaoHistorico.CONCESSAO_CREDITO.getDescricao().trim()) 
							&& !disciplina.getSituacaoFinal().trim().equals(SituacaoHistorico.CONCESSAO_CARGA_HORARIA.getDescricao().trim()) 
							&& !disciplina.getSituacaoFinal().trim().equals(SituacaoHistorico.ISENTO.getDescricao().trim())) {
						certificado.setPossuiErro(Boolean.TRUE);
						certificado.setMotivoErro("Aluno reprovado/cursando em uma ou mais disciplina(s) até o período letivo selecionado.");						
						listaErro.add(certificado);
						break;
					}
				} else if (disciplina.getTipoDisciplina().equals("OP")) {
					if (disciplina.getSituacaoFinal().trim().equals(SituacaoHistorico.APROVADO.getDescricao().trim()) 
							&& disciplina.getSituacaoFinal().trim().equals(SituacaoHistorico.APROVADO_POR_EQUIVALENCIA.getDescricao().trim()) 
							&& disciplina.getSituacaoFinal().trim().equals(SituacaoHistorico.APROVADO_COM_DEPENDENCIA.getDescricao().trim())
							&& disciplina.getSituacaoFinal().trim().equals(SituacaoHistorico.APROVADO_APROVEITAMENTO.getDescricao().trim()) 
							&& disciplina.getSituacaoFinal().trim().equals(SituacaoHistorico.CONCESSAO_CREDITO.getDescricao().trim()) 
							&& disciplina.getSituacaoFinal().trim().equals(SituacaoHistorico.CONCESSAO_CARGA_HORARIA.getDescricao().trim()) 
							&& disciplina.getSituacaoFinal().trim().equals(SituacaoHistorico.ISENTO.getDescricao().trim())) {
						cargaHorariaOptativa += Integer.valueOf(disciplina.getCargaHoraria());					
					}
				}
			}
			if(cargaHorariaOptativa < certificado.getPeriodoLetivoVO().getNumeroCargaHorariaOptativa()){
				certificado.setPossuiErro(Boolean.TRUE);
				certificado.setMotivoErro("Aluno não cumpriu a carga horária optativa mínima exigida ("+cargaHorariaOptativa+" de "+certificado.getPeriodoLetivoVO().getNumeroCargaHorariaOptativa()+")");
				listaErro.add(certificado);
			}
		}
    	return listaErro;
    }
    
    public List<CertificadoCursoExtensaoRelVO> executarConsultaParametrizada(List<MatriculaVO> listaMatricula, CertificadoCursoExtensaoRelVO certificadoCursoExtensaoRelVO, String tipoLayout) throws Exception {
    	String separador = "";        
    	StringBuilder string = new StringBuilder("");
    	string.append(" SELECT DISTINCT pessoa.nome AS pessoanome, pessoa.rg as rg, pessoa.orgaoemissor, pessoa.estadoemissaorg, curso.nome AS cursonome, curso.nrregistrointerno, ");
    	string.append("	periodoletivoativounidadeensinocurso.datainicioperiodoletivo, periodoletivoativounidadeensinocurso.datafimperiodoletivo, gradecurricular.cargahoraria, matriculaperiodo.matricula,");
        string.append(" unidadeensino.nome AS unidadeensinonome, cidade.nome AS cidadenome, estado.sigla AS estadosigla from matriculaperiodo");
        string.append(" INNER JOIN matricula ON matricula.matricula = matriculaperiodo.matricula");
        string.append(" INNER JOIN pessoa ON pessoa.codigo = matricula.aluno");
        string.append(" INNER JOIN processomatricula ON processomatricula.codigo = matriculaperiodo.processomatricula");
        string.append(" INNER JOIN processomatriculacalendario ON processomatriculacalendario.processomatricula = processomatricula.codigo");
        string.append(" INNER JOIN periodoletivoativounidadeensinocurso ON periodoletivoativounidadeensinocurso.codigo = processomatriculacalendario.periodoletivoativounidadeensinocurso");
        string.append(" INNER JOIN gradecurricular ON gradecurricular.codigo = matricula.gradecurricularatual");
        string.append(" INNER JOIN curso ON curso.codigo = gradecurricular.curso");
        string.append(" INNER JOIN unidadeensino ON unidadeensino.codigo = matricula.unidadeensino");
        string.append(" INNER JOIN cidade ON cidade.codigo = unidadeensino.cidade");
        string.append(" INNER JOIN estado ON estado.codigo = cidade.estado ");
        string.append(" WHERE matriculaperiodo.matricula in(");
        for (MatriculaVO matricula : listaMatricula) {
            string.append(separador).append("'").append(matricula.getMatricula()).append("'");
            separador = ",";
        }
        string.append(") ");
        string.append(" and  case when curso.periodicidade in ('AN','SE')  then  (matriculaperiodo.codigo =  (select matp.codigo  from matriculaperiodo  matp where matp.matricula = matricula.matricula     order by (matp.ano || '/' || matp.semestre) desc limit 1 ) ) else 1=1 end  " ) ;
    	
//        string.append(" AND datainicioperiodoletivo IS NOT NULL ");
//        string.append(" AND datafimperiodoletivo IS NOT NULL ");
        string.append(" ORDER BY pessoa.nome ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(string.toString());

        return montarDadosConsulta(tabelaResultado, certificadoCursoExtensaoRelVO);
    }

    public List<CertificadoCursoExtensaoRelVO> montarDadosConsulta(SqlRowSet tabelaResultado, CertificadoCursoExtensaoRelVO certificadoCursoExtensaoRelVO) throws Exception {
        List<CertificadoCursoExtensaoRelVO> vetResultado = new ArrayList<CertificadoCursoExtensaoRelVO>(0);
        while (tabelaResultado.next()) {
            CertificadoCursoExtensaoRelVO obj = new CertificadoCursoExtensaoRelVO();
            obj.setFuncionarioPrincipalVO(certificadoCursoExtensaoRelVO.getFuncionarioPrincipalVO());
            obj.setFuncionarioSecundarioVO(certificadoCursoExtensaoRelVO.getFuncionarioSecundarioVO());
            obj.setCargoFuncionarioPrincipal(certificadoCursoExtensaoRelVO.getCargoFuncionarioPrincipal());
            obj.setCargoFuncionarioSecundario(certificadoCursoExtensaoRelVO.getCargoFuncionarioSecundario());     
            obj.setUnidadeEnsinoVO(certificadoCursoExtensaoRelVO.getUnidadeEnsinoVO());
           	vetResultado.add(montarDados(tabelaResultado, obj));
        }
        return vetResultado;
    }
    
    public CertificadoCursoExtensaoRelVO montarDados(SqlRowSet dadosSQL, CertificadoCursoExtensaoRelVO obj) throws Exception {
        obj.setNomeAluno(dadosSQL.getString("pessoanome"));
        obj.setNomeCurso(dadosSQL.getString("cursonome"));
        obj.setRgAluno(dadosSQL.getString("rg"));
        obj.setOrgaoEmissorRgAluno(dadosSQL.getString("orgaoemissor"));
        obj.setEstadoEmissorRgAluno(dadosSQL.getString("estadoemissaorg"));
        obj.setMatricula(dadosSQL.getString("matricula"));
        obj.setMatriculaVO(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), obj.getUnidadeEnsinoVO().getCodigo(), NivelMontarDados.BASICO, new UsuarioVO()));
        obj.setCargaHoraria(dadosSQL.getString("cargahoraria"));
        obj.setPeriodoCursado(Uteis.getData(dadosSQL.getDate("datainicioperiodoletivo"), "MMMM").toLowerCase() + " a " + Uteis.getData(dadosSQL.getDate("datafimperiodoletivo"), "MMMM ' de ' yyyy").toLowerCase());
        obj.setCidadeDataAtual(Uteis.getDataCidadeDiaMesPorExtensoEAno(dadosSQL.getString("cidadenome"), new Date(), false));
        obj.setMantida(dadosSQL.getString("unidadeensinonome"));
        obj.setCredenciamento(dadosSQL.getString("nrregistrointerno"));
    	obj.setDataAtualPorExtenso(Uteis.getDataCidadeDiaMesPorExtensoEAno(dadosSQL.getString("cidadenome"),new Date(),false));
        return obj;
    }

    public void montarDadosFuncionarios(CertificadoCursoExtensaoRelVO certificadoCursoExtensaoRelVO, UsuarioVO usuarioVO) throws Exception {
    	if(Uteis.isAtributoPreenchido(certificadoCursoExtensaoRelVO.getFuncionarioPrincipalVO())){
    		getFacadeFactory().getFuncionarioFacade().carregarDados(certificadoCursoExtensaoRelVO.getFuncionarioPrincipalVO(), NivelMontarDados.BASICO, usuarioVO);
    		certificadoCursoExtensaoRelVO.getFuncionarioPrincipalVO().setArquivoAssinaturaVO(getFacadeFactory().getArquivoFacade().consultarAssinaturaDigitalFuncionarioPorCodigoFuncionario(certificadoCursoExtensaoRelVO.getFuncionarioPrincipalVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOS_CAMINHO_ARQUIVO_MINIMO, usuarioVO));
    	}
    	if(Uteis.isAtributoPreenchido(certificadoCursoExtensaoRelVO.getCargoFuncionarioPrincipal())){
    		certificadoCursoExtensaoRelVO.setCargoFuncionarioPrincipal(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(certificadoCursoExtensaoRelVO.getCargoFuncionarioPrincipal().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO));
    	}
        if(Uteis.isAtributoPreenchido(certificadoCursoExtensaoRelVO.getFuncionarioSecundarioVO())){
        	getFacadeFactory().getFuncionarioFacade().carregarDados(certificadoCursoExtensaoRelVO.getFuncionarioSecundarioVO(), NivelMontarDados.BASICO, usuarioVO);
        	certificadoCursoExtensaoRelVO.getFuncionarioSecundarioVO().setArquivoAssinaturaVO(getFacadeFactory().getArquivoFacade().consultarAssinaturaDigitalFuncionarioPorCodigoFuncionario(certificadoCursoExtensaoRelVO.getFuncionarioSecundarioVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOS_CAMINHO_ARQUIVO_MINIMO, usuarioVO));
        }
        if(Uteis.isAtributoPreenchido(certificadoCursoExtensaoRelVO.getCargoFuncionarioSecundario())){
        certificadoCursoExtensaoRelVO.setCargoFuncionarioSecundario(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(certificadoCursoExtensaoRelVO.getCargoFuncionarioSecundario().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO));
        }
    }

    public void montarCargaHorariaPorExtenso(List<CertificadoCursoExtensaoRelVO> listaCertificadoCursoExtensaoRelVO){
        Extenso extenso = new Extenso();
        for(CertificadoCursoExtensaoRelVO certificadoCursoExtensaoRelVO: listaCertificadoCursoExtensaoRelVO) {
        	extenso.setUsarHoras(true);
        	extenso.setNumber(new BigDecimal(certificadoCursoExtensaoRelVO.getCargaHoraria()));
            certificadoCursoExtensaoRelVO.setCargaHoraria(certificadoCursoExtensaoRelVO.getCargaHoraria()+" ("+extenso.toString()+")");
        }
    }
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
    public HashMap<String, Object> realizarMontagemRelatorioPorTextoPadrao (List<CertificadoCursoExtensaoRelVO> certificadoCursoExtensaoRelVOs, List<File> listaArquivos, TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO, PlanoEnsinoVO plano, GradeDisciplinaVO gradeDisciplinaVO, RequerimentoVO requerimentoVO, ConfiguracaoGeralSistemaVO config, Boolean gerarNovoArquivoAssinado, Boolean persistirDocumento, UsuarioVO usuario) throws Exception{
    	HashMap<String, Object> hashMap = new HashMap<String, Object>();
    	ImpressaoContratoVO impressaoContratoVO =null;
    	for (CertificadoCursoExtensaoRelVO certificado : certificadoCursoExtensaoRelVOs) {
			impressaoContratoVO = new ImpressaoContratoVO();
			impressaoContratoVO.setGerarNovoArquivoAssinado(gerarNovoArquivoAssinado);
			impressaoContratoVO.setTipoTextoEnum(TipoDoTextoImpressaoContratoEnum.TEXTO_PADRAO_DECLARACAO);			
			textoPadraoDeclaracaoVO.setTexto(getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultaTextoDoTextoPadraoPorChavePrimaria(textoPadraoDeclaracaoVO.getCodigo(), usuario));
			impressaoContratoVO.setCertificadoCursoExtensaoRelVO(certificado);
			impressaoContratoVO.setListaDisciplinasAprovadasPeriodoLetivo(certificado.getCertificadoCursoExtensaoDisciplinasRelVOs());
			impressaoContratoVO.setFuncionarioPrincipalVO(certificado.getFuncionarioPrincipalVO());
			impressaoContratoVO.setCargoFuncionarioPrincipal(certificado.getCargoFuncionarioPrincipal());
			impressaoContratoVO.setFuncionarioSecundarioVO(certificado.getFuncionarioSecundarioVO());
			impressaoContratoVO.setCargoFuncionarioSecundario(certificado.getCargoFuncionarioSecundario());		
			impressaoContratoVO.setRequerimentoVO(requerimentoVO);
			if (!gradeDisciplinaVO.getCodigo().equals(0) || !gradeDisciplinaVO.getDisciplina().getCodigo().equals(0)) {
				impressaoContratoVO.setPlanoEnsinoVO(plano);
				impressaoContratoVO.setDisciplinaVO(gradeDisciplinaVO.getDisciplina());
				impressaoContratoVO.setGradeDisciplinaVO(gradeDisciplinaVO);
				if(!certificado.getCertificadoCursoExtensaoDisciplinasRelVOs().isEmpty()){
					impressaoContratoVO.getProfessor().getPessoa().setNome(certificado.getCertificadoCursoExtensaoDisciplinasRelVOs().get(0).getNomeProfessor());					
				}
				if (Uteis.isAtributoPreenchido(impressaoContratoVO.getDisciplinaVO())) {
					impressaoContratoVO.setHistoricoVO(montarDadosDisciplinaHistorico(impressaoContratoVO.getDisciplinaVO(), certificado.getMatriculaVO().getMatricula(), textoPadraoDeclaracaoVO.getTexto().contains("BimestreAnoConclusao_Disciplina"), usuario));
				}
			}
			if (Uteis.isAtributoPreenchido(impressaoContratoVO.getRequerimentoVO())) {
				if (impressaoContratoVO.getRequerimentoVO().getIsFormatoCertificadoSelecionadoImpresso()) {
					persistirDocumento = false;
				}
			}
			hashMap.put("texto",getFacadeFactory().getImpressaoContratoFacade().montarDadosContratoTextoPadrao(certificado.getMatriculaVO(), impressaoContratoVO, textoPadraoDeclaracaoVO, config, usuario));
//			if(textoPadraoDeclaracaoVO.getTipoDesigneTextoEnum().isPdf()){
//				String caminhoRelatorio = getFacadeFactory().getImpressaoDeclaracaoFacade().executarValidacaoImpressaoEmPdf(impressaoContratoVO, textoPadraoDeclaracaoVO, "", persistirDocumento, config, usuario);
//				listaArquivos.add(new File(getCaminhoPastaWeb() + File.separator + "relatorio" + File.separator + caminhoRelatorio));
//				textoPadraoDeclaracaoVO.getParametrosRel().clear();
//				hashMap.put("impressaoContratoExistente", impressaoContratoVO.getImpressaoContratoExistente());
//			}else{
//				hashMap.put("impressaoContratoExistente", false);
//			}
			if (textoPadraoDeclaracaoVO.getTipoDesigneTextoEnum().isHtml()){
				impressaoContratoVO.setImpressaoPdf(false);	
			} else {
				impressaoContratoVO.setImpressaoPdf(true);
			}
//			if(persistirDocumento && impressaoContratoVO.getGerarNovoArquivoAssinado()) {
//				getFacadeFactory().getImpressaoDeclaracaoFacade().gravarImpressaoContrato(impressaoContratoVO);
//			}
		}
    	return hashMap;
    }

    public static String getDesignIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
    }

    public static String getCaminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
    }

    public static String getIdEntidade() {
        return ("CertificadoCursoExtensaoRel");
    }
    
    public void validarDadosPeriodoLetivoEmissaoCertificado(Integer periodoLetivo) throws Exception {
    	if (periodoLetivo.equals(0)) {
    		throw new Exception("O campo PERÍODO LETIVO deve ser informado.");
    	}
    }
    
    private HistoricoVO montarDadosDisciplinaHistorico(DisciplinaVO disciplinaVO, String matricula, Boolean trazerBimestreAnoConclusaoDiscplina, UsuarioVO usuarioVO) throws Exception {
    	HistoricoVO historicoVO = null;
    	try {
    		historicoVO = getFacadeFactory().getHistoricoFacade()
    				.consultaRapidaPorMatricula_Disciplina_Turma(matricula, disciplinaVO.getCodigo(), null, "", "", trazerBimestreAnoConclusaoDiscplina, false, usuarioVO); 
    		historicoVO.setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);
    		getFacadeFactory().getHistoricoFacade().carregarDadosHorarioAulaAluno(historicoVO, true);
    		getFacadeFactory().getHistoricoFacade().carregarDados(historicoVO, NivelMontarDados.BASICO, usuarioVO);
    		disciplinaVO.setFrequencia(historicoVO.getFrequencia_Apresentar());
			Date dataFim = historicoVO.getData();
			Date dataInicio = historicoVO.getDataPrimeiraAula();				
			if (dataInicio == null) {
				disciplinaVO.setDiaInicio(0);
			} else {
				disciplinaVO.setDiaInicio(Uteis.getDiaMesData(dataInicio));
			}
			if (dataFim == null) {
				disciplinaVO.setDiaFim(0);
				disciplinaVO.setAno("");
				disciplinaVO.setMes("");
			} else {
				disciplinaVO.setDiaFim(Uteis.getDiaMesData(dataFim));
				disciplinaVO.setAno(String.valueOf(Uteis.getAnoData(dataFim)));
				String mes = String.valueOf(MesAnoEnum.getMesData(dataFim));
				disciplinaVO.setMes(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(mes != null ? mes.toLowerCase() : null));
			}
		} catch (Exception e) {
			throw e;
		}
    	return historicoVO;
    }

	@Override
	public void validarRegraEmissao(List<CertificadoCursoExtensaoRelVO> certificadoCursoExtensaoRelVOs, List<CertificadoCursoExtensaoRelVO> certificadoCursoExtensaoRelVOsErro ,  UsuarioVO usuarioVO, String filtro ) throws Exception {
//		List<RegraEmissaoVO> regraEmissaoVOs = getFacadeFactory().getRegraEmissaoInterfaceFacade().consultarTodasRegrasEmissao(usuarioVO);
//		if(regraEmissaoVOs != null) {
//			for(CertificadoCursoExtensaoRelVO certificadoCursoExtensaoRelVO : certificadoCursoExtensaoRelVOs ) {
//				RegraEmissaoVO regraEmissao = regraEmissaoVOs.stream().filter(r -> r.getNivelEducacional().equals(certificadoCursoExtensaoRelVO.getMatriculaVO().getCurso().getNivelEducacional())).findFirst().orElse(null);
//				if(regraEmissao != null && Uteis.isAtributoPreenchido(regraEmissao.getCodigo())){
//					verificarRegraEmissao(regraEmissao, usuarioVO, certificadoCursoExtensaoRelVO, filtro, certificadoCursoExtensaoRelVOs,certificadoCursoExtensaoRelVOsErro);
//				}
//			}	
//		}
	}
	
	private void verificarRegraEmissao( UsuarioVO usuarioVO, CertificadoCursoExtensaoRelVO certificadoCursoExtensaoRelVO, String filtro,List<CertificadoCursoExtensaoRelVO> certificadoCursoExtensaoRelVOs ,List<CertificadoCursoExtensaoRelVO> certificadoCursoExtensaoRelVOsErro ) throws Exception {
		
//		MatriculaVO matriculaVO = certificadoCursoExtensaoRelVO.getMatriculaVO();
//		String documentoPendente = getFacadeFactory().getDocumetacaoMatriculaFacade().consultarExistenciaPendenciaDocumentacaoPorMatricula(matriculaVO.getAluno().getCodigo(), matriculaVO.getMatricula(), new ArrayList<TipoDocumentoVO>(),true);
//				
//		if(filtro.equals("aluno")) {
//			if(regraEmissao.getValidarMatrizCurricularIntegralizado() &&
//			  !getFacadeFactory().getMatriculaFacade().isMatriculaIntegralizada(matriculaVO,matriculaVO.getGradeCurricularAtual().getCodigo(),usuarioVO, null)) { throw
//			  new ConsistirException("O Aluno não possui matrícula integralizada."); 
//			}if(regraEmissao.getValidarNotaTCC() && (matriculaVO.getNotaMonografia() == null || matriculaVO.getNotaMonografia().compareTo(regraEmissao.getNotaTCC()) < 0)){
//				throw new ConsistirException("O Aluno não possui nota mínima de TCC.");
//			}if(regraEmissao.getValidarDocumentosEntregues() && documentoPendente != null){
//				throw new ConsistirException("O Aluno possui documentos pedentes para ser entregues.");
//			}if(Uteis.isAtributoPreenchido(regraEmissao.getTipoContrato()) && !regraEmissao.getTipoContrato().equals(matriculaVO.getTipoMatricula())){
//				throw new ConsistirException("Tipo de Contrato não é válido.");
//			}if(!verificarRegraEmissaoUnidadeEnsio(regraEmissao.getRegraEmissaoUnidadeEnsinoVOs(), certificadoCursoExtensaoRelVO.getUnidadeEnsinoVO())) {
//				throw new ConsistirException(" A Unidade de ensino não é válida para Regra de Emissão.");
//			}
//		}else {
//			if(regraEmissao.getValidarMatrizCurricularIntegralizado() 
//				&& !getFacadeFactory().getMatriculaFacade().isMatriculaIntegralizada(matriculaVO,matriculaVO.getGradeCurricularAtual().getCodigo(),usuarioVO, null)){
//			    certificadoCursoExtensaoRelVO.setPossuiErro(Boolean.TRUE);
//				certificadoCursoExtensaoRelVO.setMotivoErro("O Aluno não possui matrícula integralizada.");		
//			}if(regraEmissao.getValidarNotaTCC() && (matriculaVO.getNotaMonografia() == null || matriculaVO.getNotaMonografia().compareTo(regraEmissao.getNotaTCC()) < 0)){
//				
//				certificadoCursoExtensaoRelVO.setPossuiErro(Boolean.TRUE);
//				certificadoCursoExtensaoRelVO.setMotivoErro("O Aluno não possui nota mínima de TCC.");		
//			}if(regraEmissao.getValidarDocumentosEntregues() && documentoPendente != null){
//				
//				certificadoCursoExtensaoRelVO.setPossuiErro(Boolean.TRUE);
//				certificadoCursoExtensaoRelVO.setMotivoErro("O Aluno possui documentos pedentes para ser entregues.");		
//			}if(Uteis.isAtributoPreenchido(regraEmissao.getTipoContrato()) && !regraEmissao.getTipoContrato().equals(matriculaVO.getTipoMatricula())){
//			
//				certificadoCursoExtensaoRelVO.setPossuiErro(Boolean.TRUE);
//				certificadoCursoExtensaoRelVO.setMotivoErro("Tipo de Contrato não é válido.");		
//			}if(!verificarRegraEmissaoUnidadeEnsio(regraEmissao.getRegraEmissaoUnidadeEnsinoVOs(), certificadoCursoExtensaoRelVO.getUnidadeEnsinoVO())) {
//				
//				certificadoCursoExtensaoRelVO.setPossuiErro(Boolean.TRUE);
//				certificadoCursoExtensaoRelVO.setMotivoErro(" A Unidade de ensino não é válida para Regra de Emissão.");		
//			}
//			if(certificadoCursoExtensaoRelVO.getPossuiErro()) {
//				certificadoCursoExtensaoRelVOsErro.add(certificadoCursoExtensaoRelVO);
//				certificadoCursoExtensaoRelVOs.remove(certificadoCursoExtensaoRelVO);
//			}
//		}
	}

//	private boolean verificarRegraEmissaoUnidadeEnsio(List<RegraEmissaoUnidadeEnsinoVO> regraEmissaoUnidadeEnsinoVOs, UnidadeEnsinoVO unidadeEnsinoVO) {
//		boolean isUnidadeEnsino = false;
//		if(regraEmissaoUnidadeEnsinoVOs != null) {
//			for(RegraEmissaoUnidadeEnsinoVO emissaoUnidadeEnsinoVO : regraEmissaoUnidadeEnsinoVOs){
//				if(emissaoUnidadeEnsinoVO.getUnidadeEnsinoVO().getCodigo().equals(unidadeEnsinoVO.getCodigo())) {
//					isUnidadeEnsino = true;
//				}
//			}
//		}
//		return isUnidadeEnsino;
//	}
//	
	
	@Override
	public List<CertificadoCursoExtensaoRelVO> realizarMontagemDadosRelatorioLayoutCertificadoCursoExtensao(
			List<CertificadoCursoExtensaoRelVO> listaCertificadoErro,
			CertificadoCursoExtensaoRelVO certificadoCursoExtensaoRelVO, MatriculaVO matriculaVO,
			PeriodoLetivoVO periodoLetivoVO, TurmaVO turmaVO, String tipoLayout, String filtro,
			 boolean trazerApenasDisciplinaPeriodoSelecionado,
			GradeDisciplinaVO gradeDisciplinaVO, UsuarioVO usuarioVO, Boolean trazerTodasSituacoesAprovadas,
			ProgramacaoFormaturaVO programacaoFormaturaVO, UnidadeEnsinoVO unidadeEnsinoVO,	 PlanoEnsinoVO plano,  List<CertificadoCursoExtensaoRelVO> listaCertificadoCursoExtensaoRelVOGerar)
			throws Exception {
		boolean isTextoPadrao  = tipoLayout.equals("TextoPadrao");
		List<CertificadoCursoExtensaoRelVO> certificadoCursoExtensaoRelVOsGerar  =  null ;		
		if(!isTextoPadrao &&  !filtro.equals("programacaoFormatura")) {
			certificadoCursoExtensaoRelVO.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(unidadeEnsinoVO.getCodigo(), false, usuarioVO));
		}
		
		if(isTextoPadrao) {	
			if(Uteis.isAtributoPreenchido(gradeDisciplinaVO)) {
				gradeDisciplinaVO =  getFacadeFactory().getGradeDisciplinaFacade().consultarPorChavePrimaria(gradeDisciplinaVO.getCodigo(), usuarioVO);				
			}			
			if (!Uteis.isAtributoPreenchido(gradeDisciplinaVO) || Uteis.isAtributoPreenchido(gradeDisciplinaVO.getDisciplina())) {
				gradeDisciplinaVO.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(gradeDisciplinaVO.getDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
			}		
			
		}		
			
		certificadoCursoExtensaoRelVOsGerar = getFacadeFactory().getCertificadoCursoExtensaoRelFacade().criarObjeto(certificadoCursoExtensaoRelVO,matriculaVO, periodoLetivoVO ,turmaVO, tipoLayout, filtro, trazerApenasDisciplinaPeriodoSelecionado, gradeDisciplinaVO.getDisciplina(),  usuarioVO, trazerTodasSituacoesAprovadas , programacaoFormaturaVO ,listaCertificadoErro , listaCertificadoCursoExtensaoRelVOGerar);

				
		if (Uteis.isAtributoPreenchido(certificadoCursoExtensaoRelVOsGerar) && !certificadoCursoExtensaoRelVOsGerar.isEmpty()) {
			List <CertificadoCursoExtensaoRelVO > listaCertificadoErroAux  = getFacadeFactory().getCertificadoCursoExtensaoRelFacade().executarValidarSituacaoDisciplinaAprovada(certificadoCursoExtensaoRelVOsGerar);
			getFacadeFactory().getCertificadoCursoExtensaoRelFacade().validarRegraEmissao(certificadoCursoExtensaoRelVOsGerar, listaCertificadoErroAux ,usuarioVO, filtro);
			if(Uteis.isAtributoPreenchido(listaCertificadoErroAux) && !listaCertificadoErroAux.isEmpty()) {
				listaCertificadoErro.addAll(listaCertificadoErroAux);
			}
			if(certificadoCursoExtensaoRelVOsGerar.isEmpty()) {
				return new ArrayList<CertificadoCursoExtensaoRelVO>();
			}			
			
			
			for (CertificadoCursoExtensaoRelVO certificadoErro : listaCertificadoErro) {
				if (certificadoCursoExtensaoRelVOsGerar.contains(certificadoErro)) {
					certificadoCursoExtensaoRelVOsGerar.remove(certificadoErro);
				}
			}			
			if(isTextoPadrao) {				
				if (!gradeDisciplinaVO.getCodigo().equals(0) || !gradeDisciplinaVO.getDisciplina().getCodigo().equals(0)) {
					String ano = "";
					String semestre = ""; 
					if(!certificadoCursoExtensaoRelVOsGerar.get(0).getCertificadoCursoExtensaoDisciplinasRelVOs().isEmpty()){
						ano = certificadoCursoExtensaoRelVOsGerar.get(0).getCertificadoCursoExtensaoDisciplinasRelVOs().get(0).getAno();
						semestre = certificadoCursoExtensaoRelVOsGerar.get(0).getCertificadoCursoExtensaoDisciplinasRelVOs().get(0).getSemestre();
					}else{
						ano = (matriculaVO.getCurso().getPeriodicidade().equals("AN") || matriculaVO.getCurso().getPeriodicidade().equals("SE")) ?  Uteis.getAnoDataAtual4Digitos() : "" ;
						semestre= (matriculaVO.getCurso().getPeriodicidade().equals("SE")) ? Uteis.getSemestreAtual() : "" ;					
						
						
					}
					if(!gradeDisciplinaVO.getDisciplina().getCodigo().equals(0) && !certificadoCursoExtensaoRelVOsGerar.get(0).getCertificadoCursoExtensaoDisciplinasRelVOs().isEmpty()){
						gradeDisciplinaVO.setCargaHoraria(Integer.valueOf(certificadoCursoExtensaoRelVOsGerar.get(0).getCertificadoCursoExtensaoDisciplinasRelVOs().get(0).getCargaHoraria()));
					}
					plano = (getFacadeFactory().getPlanoEnsinoFacade().consultarPorUnidadeEnsinoCursoDisciplinaAnoSemestre(matriculaVO.getUnidadeEnsino().getCodigo(), matriculaVO.getCurso().getCodigo(), gradeDisciplinaVO.getDisciplina().getCodigo(), ano, semestre, null, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuarioVO));
				}			

			}
		}
		return certificadoCursoExtensaoRelVOsGerar ;
	}
	
	 private void validarRegraExistenciaHistoricoEmMatriculasVindoProgramacaoFormatura(List<CertificadoCursoExtensaoRelVO> lista,  List<CertificadoCursoExtensaoRelVO> listaErros ,List<MatriculaVO> listaMatricula) {
		   for(MatriculaVO objMatricula :  listaMatricula) {
			   Boolean existeMatriculaNaLista = lista.stream().map(CertificadoCursoExtensaoRelVO::getMatriculaVO).anyMatch( matricula -> matricula.getMatricula().equals(objMatricula.getMatricula()));
			   if(!existeMatriculaNaLista) {
				   CertificadoCursoExtensaoRelVO certificado = new CertificadoCursoExtensaoRelVO();
				   certificado.setMatriculaVO(objMatricula);
				   certificado.setPossuiErro(Boolean.TRUE);
				   certificado.setMotivoErro("Matricula possui pendencia para geração do certificado ");
				   listaErros.add(certificado);
			   }
		   }
		
	}
	

}
