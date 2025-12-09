package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.CancelamentoVO;
import negocio.comuns.academico.MotivoCancelamentoTrancamentoVO;
import negocio.comuns.academico.TrancamentoVO;
import negocio.comuns.academico.TransferenciaEntradaVO;
import negocio.comuns.academico.TransferenciaSaidaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoTransferenciaEntrada;
import negocio.comuns.utilitarias.dominios.TiposRequerimento;
import negocio.comuns.utilitarias.dominios.TiposRequerimentoRelatorio;
import relatorio.negocio.comuns.academico.OcorrenciasAlunosVO;
import relatorio.negocio.interfaces.academico.OcorrenciasAlunosRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class OcorrenciasAlunosRel extends SuperRelatorio implements OcorrenciasAlunosRelInterfaceFacade {

    public OcorrenciasAlunosRel() {
    }

//    public static void validarDados(OcorrenciasAlunosVO obj) throws Exception {
//	if(obj.getDataInicio() == null || obj.getDataFim() == null) {
//            throw new Exception("Os campos Data Início e Data Fim devem ser informados.");
//        }
//    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * relatorio.negocio.jdbc.academico.OcorrenciasAlunosRelInterfaceFacade#criarObjeto(relatorio.negocio.comuns.academico
     * .OcorrenciasAlunosVO, java.lang.String)
     */
    public List<OcorrenciasAlunosVO> criarObjeto(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, OcorrenciasAlunosVO obj, String tipoOcorrencia, String ordenarPor) throws Exception {
    	
        List<OcorrenciasAlunosVO> listaRelatorio = new ArrayList<OcorrenciasAlunosVO>(0);
        if (tipoOcorrencia.equals("TD")) {
            obj.setCancelamentoVOs(consultarCancelamentosPorData(filtroRelatorioAcademicoVO, obj.getMotivoCancelamentoTrancamentoVO(), obj.getUnidadeEnsino().getCodigo(), ordenarPor));
            obj.setTrancamentoVOs(consultarTrancamentosPorData(filtroRelatorioAcademicoVO,  obj.getMotivoCancelamentoTrancamentoVO(), obj.getUnidadeEnsino().getCodigo(), ordenarPor));
            obj.setAbandonoCursoVOs(consultarAbandonoCursoPorData(filtroRelatorioAcademicoVO, obj.getMotivoCancelamentoTrancamentoVO(), obj.getUnidadeEnsino().getCodigo(), ordenarPor));
            obj.setTransferenciaEntradaVOs(consultarTodasTransferenciasEntradaPorData(filtroRelatorioAcademicoVO, obj.getUnidadeEnsino().getCodigo(), "", ordenarPor));
            obj.setTransferenciaSaidaVOs(consultarTodasTransferenciasSaidaPorData(filtroRelatorioAcademicoVO, obj.getUnidadeEnsino().getCodigo(), ordenarPor));
            obj.setTransferenciaInternaVOs(consultarTodasTransferenciasInternaPorData(filtroRelatorioAcademicoVO, obj.getUnidadeEnsino().getCodigo(), ordenarPor));
            obj.setFormadoVOs(consultarFormadosPorData(filtroRelatorioAcademicoVO, obj.getUnidadeEnsino().getCodigo(), ordenarPor));
        } else if (tipoOcorrencia.equals(TiposRequerimento.CANCELAMENTO.getValor())) {
            obj.setCancelamentoVOs(consultarCancelamentosPorData(filtroRelatorioAcademicoVO, obj.getMotivoCancelamentoTrancamentoVO(), obj.getUnidadeEnsino().getCodigo(), ordenarPor));
        } else if (tipoOcorrencia.equals(TiposRequerimento.TRANCAMENTO.getValor())) {
            obj.setTrancamentoVOs(consultarTrancamentosPorData(filtroRelatorioAcademicoVO, obj.getMotivoCancelamentoTrancamentoVO(), obj.getUnidadeEnsino().getCodigo(), ordenarPor));
        } else if (tipoOcorrencia.equals(TiposRequerimentoRelatorio.ABANDONO_CURSO.getValor())) {
        	obj.setAbandonoCursoVOs(consultarAbandonoCursoPorData(filtroRelatorioAcademicoVO, obj.getMotivoCancelamentoTrancamentoVO(), obj.getUnidadeEnsino().getCodigo(), ordenarPor));
        } else if (tipoOcorrencia.equals(TiposRequerimento.TRANSF_INTERNA.getValor())) {
            obj.setTransferenciaInternaVOs(consultarTodasTransferenciasInternaPorData(filtroRelatorioAcademicoVO, obj.getUnidadeEnsino().getCodigo(), ordenarPor));
        } else if (tipoOcorrencia.equals(TiposRequerimento.TRANSF_ENTRADA.getValor())) {
            obj.setTransferenciaEntradaVOs(consultarTodasTransferenciasEntradaPorData(filtroRelatorioAcademicoVO, obj.getUnidadeEnsino().getCodigo(), TipoTransferenciaEntrada.EXTERNA.getValor(), ordenarPor));
        } else if (tipoOcorrencia.equals(TiposRequerimento.TRANSF_SAIDA.getValor())) {
            obj.setTransferenciaSaidaVOs(consultarTodasTransferenciasSaidaPorData(filtroRelatorioAcademicoVO, obj.getUnidadeEnsino().getCodigo(), ordenarPor));
	    } else if (tipoOcorrencia.equals(TiposRequerimentoRelatorio.FORMADO.getValor())) {
	        obj.setFormadoVOs(consultarFormadosPorData(filtroRelatorioAcademicoVO, obj.getUnidadeEnsino().getCodigo(), ordenarPor));
	    }
        
        if(tipoOcorrencia.equals("TD")) {
            if(!obj.getCancelamentoVOs().isEmpty() 
            		|| !obj.getTrancamentoVOs().isEmpty() 
            		|| !obj.getTransferenciaEntradaVOs().isEmpty() 
            		|| !obj.getTransferenciaSaidaVOs().isEmpty()
            		|| !obj.getTransferenciaInternaVOs().isEmpty()
            		|| !obj.getAbandonoCursoVOs().isEmpty()
            		|| !obj.getFormadoVOs().isEmpty()
            		) {
                obj.setQtdeCancelamento(obj.getCancelamentoVOs().size());
                obj.setQtdeTrancamento(obj.getTrancamentoVOs().size());
                obj.setQtdeTransferenciaEntrada(obj.getTransferenciaEntradaVOs().size());
                obj.setQtdeTransferenciaSaida(obj.getTransferenciaSaidaVOs().size());
                obj.setQtdeTransferenciaInterna(obj.getTransferenciaInternaVOs().size());
                obj.setQtdeAbandonoCurso(obj.getAbandonoCursoVOs().size());
                obj.setQtdeFormado(obj.getFormadoVOs().size());
                obj.setQtdeTotal(obj.getQtdeCancelamento() + obj.getQtdeTrancamento() + obj.getQtdeTransferenciaEntrada() + obj.getQtdeTransferenciaSaida()+ obj.getQtdeTransferenciaInterna()+ obj.getQtdeAbandonoCurso() + obj.getQtdeFormado());
                listaRelatorio.add(obj);
            }
        }else if(tipoOcorrencia.equals(TiposRequerimento.CANCELAMENTO.getValor())) {
            if(!obj.getCancelamentoVOs().isEmpty()) {
                obj.setQtdeCancelamento(obj.getCancelamentoVOs().size());
                obj.setQtdeTrancamento(0);
                obj.setQtdeTransferenciaEntrada(0);
                obj.setQtdeTransferenciaSaida(0);
                obj.setQtdeTotal(obj.getQtdeCancelamento());
                obj.setQtdeTransferenciaInterna(0);
                obj.setQtdeAbandonoCurso(0);
                obj.setQtdeFormado(0);
                listaRelatorio.add(obj);
            }
        }else if(tipoOcorrencia.equals(TiposRequerimento.TRANCAMENTO.getValor())) {
            if(!obj.getTrancamentoVOs().isEmpty()) {
                obj.setQtdeCancelamento(0);
                obj.setQtdeTrancamento(obj.getTrancamentoVOs().size());
                obj.setQtdeTransferenciaEntrada(0);
                obj.setQtdeTransferenciaSaida(0);
                obj.setQtdeTransferenciaInterna(0);
                obj.setQtdeAbandonoCurso(0);
                obj.setQtdeTotal(obj.getQtdeTrancamento());
                obj.setQtdeFormado(0);
                listaRelatorio.add(obj);
            }
        }else if(tipoOcorrencia.equals(TiposRequerimentoRelatorio.ABANDONO_CURSO.getValor())) {
        	if(!obj.getAbandonoCursoVOs().isEmpty()) {
        		obj.setQtdeCancelamento(0);
        		obj.setQtdeTrancamento(0);
        		obj.setQtdeTransferenciaEntrada(0);
        		obj.setQtdeTransferenciaSaida(0);
        		obj.setQtdeTransferenciaInterna(0);
        		obj.setQtdeAbandonoCurso(obj.getAbandonoCursoVOs().size());
        		obj.setQtdeTotal(obj.getQtdeAbandonoCurso());
        		obj.setQtdeFormado(0);
        		listaRelatorio.add(obj);
        	}
        }else if(tipoOcorrencia.equals(TiposRequerimento.TRANSF_INTERNA.getValor())) {
            if(!obj.getTransferenciaInternaVOs().isEmpty()) {
                obj.setQtdeCancelamento(0);
                obj.setQtdeTrancamento(0);
                obj.setQtdeTransferenciaEntrada(0);
                obj.setQtdeTransferenciaSaida(0);
                obj.setQtdeAbandonoCurso(0);
                obj.setQtdeTransferenciaInterna(obj.getTransferenciaInternaVOs().size());
                obj.setQtdeTotal(obj.getQtdeTransferenciaInterna());
                obj.setQtdeFormado(0);
                listaRelatorio.add(obj);
            }
        }else if(tipoOcorrencia.equals(TiposRequerimento.TRANSF_ENTRADA.getValor())) {
            if(!obj.getTransferenciaEntradaVOs().isEmpty()) {
                obj.setQtdeCancelamento(0);
                obj.setQtdeTrancamento(0);
                obj.setQtdeTransferenciaEntrada(obj.getTransferenciaEntradaVOs().size());
                obj.setQtdeTransferenciaSaida(0);
                obj.setQtdeTransferenciaInterna(0);
                obj.setQtdeAbandonoCurso(0);
                obj.setQtdeTotal(obj.getQtdeTransferenciaEntrada());
                obj.setQtdeFormado(0);
                listaRelatorio.add(obj);
            }
        }else if(tipoOcorrencia.equals(TiposRequerimento.TRANSF_SAIDA.getValor())) {
            if(!obj.getTransferenciaSaidaVOs().isEmpty()) {
                obj.setQtdeCancelamento(0);
                obj.setQtdeTrancamento(0);
                obj.setQtdeTransferenciaEntrada(0);
                obj.setQtdeTransferenciaSaida(obj.getTransferenciaSaidaVOs().size());
                obj.setQtdeTransferenciaInterna(0);
                obj.setQtdeAbandonoCurso(0);
                obj.setQtdeTotal(obj.getQtdeTransferenciaSaida());
                obj.setQtdeFormado(0);
                listaRelatorio.add(obj);
            }
	    }else if(tipoOcorrencia.equals(TiposRequerimentoRelatorio.FORMADO.getValor())) {
	        if(!obj.getFormadoVOs().isEmpty()) {
	            obj.setQtdeCancelamento(0);
	            obj.setQtdeTrancamento(0);
	            obj.setQtdeTransferenciaEntrada(0);
	            obj.setQtdeTransferenciaSaida(0);
	            obj.setQtdeTransferenciaInterna(0);
	            obj.setQtdeAbandonoCurso(0);
	            obj.setQtdeFormado(obj.getFormadoVOs().size());
	            obj.setQtdeTotal(obj.getQtdeFormado());
	            listaRelatorio.add(obj);
	        }
	    }
        
        //listaRelatorio.add(obj);
        return listaRelatorio;
    }
    
    

    private List<CancelamentoVO> consultarCancelamentosPorData(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, MotivoCancelamentoTrancamentoVO motivoCancelamentoTrancamentoVO, Integer unidadeEnsino, String ordenarPor) throws Exception {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT distinct pessoa.nome AS \"pessoa.nome\", pessoa.telefoneres, pessoa.celular, matricula.matricula, ");
        sqlStr.append(" case when cancelamento.codigo is not null then cancelamento.data else matriculaperiodo.datafechamentomatriculaperiodo end as data, ");
        sqlStr.append(" curso.nome AS \"curso.nome\", cancelamento.justificativa, cancelamento.descricao, usuario.nome AS \"responsavel.nome\", motivocancelamentotrancamento.nome as motivo, ");
        sqlStr.append(" unidadeensino.nome AS \"unidadeensino\", matriculaperiodo.datafechamentomatriculaperiodo, turma.identificadorturma as turma ");
        sqlStr.append(" FROM matricula ");        
        sqlStr.append(" INNER JOIN matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.situacaomatriculaperiodo = 'CA' ");
        if(filtroRelatorioAcademicoVO.getFiltrarPorPeriodoData()){
        	sqlStr.append(" and matriculaperiodo.codigo = (select codigo from matriculaperiodo mp  WHERE mp.matricula = matricula.matricula and mp.datafechamentomatriculaperiodo::DATE >= '");
        	sqlStr.append(Uteis.getDataJDBC(filtroRelatorioAcademicoVO.getDataInicio()));
        	sqlStr.append("' AND mp.datafechamentomatriculaperiodo::DATE <=  '");
        	sqlStr.append(Uteis.getDataJDBC(filtroRelatorioAcademicoVO.getDataTermino()));
        	sqlStr.append("' and mp.situacaomatriculaperiodo = 'CA' order by (mp.ano || mp.semestre) desc, mp.codigo desc limit 1 ");
        	sqlStr.append(") ");
        }
        
        sqlStr.append(" INNER JOIN turma on turma.codigo = matriculaperiodo.turma ");
        sqlStr.append(" INNER JOIN pessoa on pessoa.codigo = matricula.aluno ");
        sqlStr.append(" INNER JOIN curso on curso.codigo = matricula.curso ");
        sqlStr.append(" left JOIN cancelamento on matricula.matricula = cancelamento.matricula ");
        sqlStr.append(" left JOIN motivocancelamentotrancamento on motivocancelamentotrancamento.codigo = cancelamento.motivocancelamentotrancamento ");
        sqlStr.append(" left JOIN usuario on usuario.codigo = cancelamento.responsavelautorizacao ");
        sqlStr.append(" inner JOIN unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
        sqlStr.append(" WHERE 1=1 ");
        if(unidadeEnsino != null && unidadeEnsino != 0) {
            sqlStr.append(" AND matricula.unidadeensino = ");
            sqlStr.append(unidadeEnsino);
        }
        if(Uteis.isAtributoPreenchido(motivoCancelamentoTrancamentoVO)){
        	sqlStr.append(" AND cancelamento.motivoCancelamentoTrancamento = ").append(motivoCancelamentoTrancamentoVO.getCodigo());
        }
        if(filtroRelatorioAcademicoVO.getFiltrarPorPeriodoData()){
        	sqlStr.append(" and case when cancelamento.codigo is not null then ");
            sqlStr.append(" cancelamento.data >= '").append(Uteis.getDataJDBC(filtroRelatorioAcademicoVO.getDataInicio())).append("' ");
            sqlStr.append(" and cancelamento.data <= '").append(Uteis.getDataJDBC(filtroRelatorioAcademicoVO.getDataTermino())).append("' ");
            sqlStr.append(" else matriculaperiodo.datafechamentomatriculaperiodo >= '").append(Uteis.getDataJDBC(filtroRelatorioAcademicoVO.getDataInicio())).append("' ");
            sqlStr.append(" and matriculaperiodo.datafechamentomatriculaperiodo <= '").append(Uteis.getDataJDBC(filtroRelatorioAcademicoVO.getDataTermino())).append("' ");
            sqlStr.append(" end ");
        }
        if(filtroRelatorioAcademicoVO.getFiltrarPorAnoSemestre()){
    		sqlStr.append(" AND case when cancelamento.codigo is not null then ");
    		sqlStr.append(" extract(year from cancelamento.data) >= '").append(filtroRelatorioAcademicoVO.getAno()).append("' ");
    		if (filtroRelatorioAcademicoVO.getSemestre().equals("1")) {
    			sqlStr.append(" and extract(month from cancelamento.data) <= '7' ");
    		} else {
    			sqlStr.append(" and extract(month from cancelamento.data) > '7' ");
    		}
    		sqlStr.append(" else matriculaperiodo.ano = '").append(filtroRelatorioAcademicoVO.getAno()).append("' ");
    		sqlStr.append(" and matriculaperiodo.semestre = '").append(filtroRelatorioAcademicoVO.getSemestre()).append("' end ");
    		
//    		sqlStr.append(" and matriculaperiodo.ano = '").append(filtroRelatorioAcademicoVO.getAno()).append("' and matriculaperiodo.semestre = '").append(filtroRelatorioAcademicoVO.getSemestre()).append("' ");
    	}        
    	if(filtroRelatorioAcademicoVO.getFiltrarPorAno()){
    		sqlStr.append(" AND case when cancelamento.codigo is not null then ");
    		sqlStr.append(" extract(year from cancelamento.data) >= '").append(filtroRelatorioAcademicoVO.getAno()).append("' ");
    		sqlStr.append(" else matriculaperiodo.ano = '").append(filtroRelatorioAcademicoVO.getAno()).append("' end ");
//    		sqlStr.append(" and matriculaperiodo.ano = '").append(filtroRelatorioAcademicoVO.getAno()).append("'  ");
    	}
    	if (ordenarPor.equals("data")) {
    		sqlStr.append(" ORDER BY motivocancelamentotrancamento.nome, unidadeensino, matriculaperiodo.datafechamentomatriculaperiodo, pessoa.nome");    		
    	} else if (ordenarPor.equals("aluno")) {
    		sqlStr.append(" ORDER BY motivocancelamentotrancamento.nome, unidadeensino, pessoa.nome, matriculaperiodo.datafechamentomatriculaperiodo ");
    	} else {
    		sqlStr.append(" ORDER BY motivocancelamentotrancamento.nome, unidadeensino, turma.identificadorturma, pessoa.nome, matriculaperiodo.datafechamentomatriculaperiodo");
    	}
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapidaCancelamento(tabelaResultado);
    }

    public List<CancelamentoVO> montarDadosConsultaRapidaCancelamento(SqlRowSet tabelaResultado) throws Exception {
        List<CancelamentoVO> vetResultado = new ArrayList<CancelamentoVO>(0);
        while (tabelaResultado.next()) {
            CancelamentoVO obj = new CancelamentoVO();
            montarDadosCancelamento(obj, tabelaResultado);
            vetResultado.add(obj);
            if (tabelaResultado.getRow() == 0) {
                return vetResultado;
            }
        }
        return vetResultado;
    }

    private void montarDadosCancelamento(CancelamentoVO obj, SqlRowSet dadosSQL) throws Exception {
        obj.getMatricula().getAluno().setNome(dadosSQL.getString("pessoa.nome"));
        obj.getMatricula().setMatricula(dadosSQL.getString("matricula"));
        obj.getMatricula().getAluno().setTelefoneRes(dadosSQL.getString("telefoneres"));
        obj.getMatricula().getAluno().setCelular(dadosSQL.getString("celular"));
        obj.getMatricula().getCurso().setNome(dadosSQL.getString("curso.nome"));
        obj.getMatricula().getUnidadeEnsino().setNome(dadosSQL.getString("unidadeensino"));
        obj.setData(dadosSQL.getDate("data"));
        obj.setJustificativa(dadosSQL.getString("justificativa"));
        obj.getResponsavelAutorizacao().setNome(dadosSQL.getString("responsavel.nome"));
        obj.getMotivoCancelamentoTrancamento().setNome(dadosSQL.getString("motivo"));
        obj.setTurma(dadosSQL.getString("turma"));
        obj.setDescricao(dadosSQL.getString("descricao"));
    }

    
    private List<TrancamentoVO> consultarTrancamentosPorData(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, MotivoCancelamentoTrancamentoVO motivoCancelamentoTrancamentoVO, Integer unidadeEnsino, String ordenarPor) throws Exception {
    	StringBuilder sqlStr = new StringBuilder();
    	sqlStr.append(" SELECT distinct pessoa.nome AS \"pessoa.nome\", pessoa.telefoneres, pessoa.celular, matricula.matricula, ");
    	sqlStr.append(" case when trancamento.codigo is not null then trancamento.data else matriculaperiodo.datafechamentomatriculaperiodo end as data, ");
    	sqlStr.append(" curso.nome AS \"curso.nome\", trancamento.justificativa, trancamento.descricao, usuario.nome AS \"responsavel.nome\", motivocancelamentotrancamento.nome as motivo, ");
    	sqlStr.append(" unidadeensino.nome AS \"unidadeensino\", matriculaperiodo.datafechamentomatriculaperiodo, turma.identificadorturma as turma ");
    	sqlStr.append(" FROM matricula ");        
    	sqlStr.append(" INNER JOIN matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.situacaomatriculaperiodo = 'TR' ");
    	if(filtroRelatorioAcademicoVO.getFiltrarPorPeriodoData()){
    		sqlStr.append(" and matriculaperiodo.codigo = (select mp.codigo from matriculaperiodo mp ");
    		sqlStr.append(" left join trancamento on trancamento.matriculaperiodo = matriculaperiodo.codigo ");
    		sqlStr.append(" WHERE mp.matricula = matricula.matricula ");
    		sqlStr.append(" and case when trancamento.matriculaperiodo is not null then ");
    		sqlStr.append(" trancamento.data >= '").append(Uteis.getDataJDBC(filtroRelatorioAcademicoVO.getDataInicio())).append("' ");
    		sqlStr.append(" and trancamento.data <= '").append(Uteis.getDataJDBC(filtroRelatorioAcademicoVO.getDataTermino())).append("' ");
    		sqlStr.append(" else ");
    		sqlStr.append(" mp.datafechamentomatriculaperiodo::DATE >= '");
    		sqlStr.append(Uteis.getDataJDBC(filtroRelatorioAcademicoVO.getDataInicio()));
    		sqlStr.append("' AND mp.datafechamentomatriculaperiodo::DATE <=  '");
    		sqlStr.append(Uteis.getDataJDBC(filtroRelatorioAcademicoVO.getDataTermino()));
    		sqlStr.append("' end order by (mp.ano || mp.semestre) desc, mp.codigo desc limit 1) ");
    	}
    	        
    	sqlStr.append(" INNER JOIN turma on turma.codigo = matriculaperiodo.turma ");
    	sqlStr.append(" INNER JOIN pessoa on pessoa.codigo = matricula.aluno ");
    	sqlStr.append(" INNER JOIN curso on curso.codigo = matricula.curso ");
    	sqlStr.append(" LEFT JOIN trancamento on trancamento.matricula = matricula.matricula ");
    	sqlStr.append(" left JOIN motivocancelamentotrancamento on motivocancelamentotrancamento.codigo = trancamento.motivocancelamentotrancamento ");
    	sqlStr.append(" LEFT JOIN usuario on usuario.codigo = trancamento.responsavelAutorizacao ");
    	sqlStr.append(" inner JOIN unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
    	sqlStr.append(" WHERE 1=1 ");
    	if(unidadeEnsino != null && unidadeEnsino != 0) {
    		sqlStr.append(" AND matricula.unidadeensino = ");
    		sqlStr.append(unidadeEnsino);
    	}
    	if(Uteis.isAtributoPreenchido(motivoCancelamentoTrancamentoVO)){
        	sqlStr.append(" AND trancamento.motivoCancelamentoTrancamento = ").append(motivoCancelamentoTrancamentoVO.getCodigo());
        }
    	if(filtroRelatorioAcademicoVO.getFiltrarPorPeriodoData()){
    		sqlStr.append(" AND case when trancamento.codigo is not null then  ");
        	sqlStr.append(" trancamento.data >= '").append(Uteis.getDataJDBC(filtroRelatorioAcademicoVO.getDataInicio())).append("' ");
        	sqlStr.append(" and trancamento.data <= '").append(Uteis.getDataJDBC(filtroRelatorioAcademicoVO.getDataTermino())).append("' ");
        	sqlStr.append(" else ");
        	sqlStr.append(" matriculaperiodo.datafechamentomatriculaperiodo::DATE >= '").append(Uteis.getDataJDBC(filtroRelatorioAcademicoVO.getDataInicio())).append("' ");
        	sqlStr.append(" and matriculaperiodo.datafechamentomatriculaperiodo::DATE <=  '").append(Uteis.getDataJDBC(filtroRelatorioAcademicoVO.getDataTermino())).append("' ");
        	sqlStr.append(" end ");
    	}
    	if(filtroRelatorioAcademicoVO.getFiltrarPorAnoSemestre()){
    		sqlStr.append(" AND case when trancamento.codigo is not null then ");
    		sqlStr.append(" extract(year from trancamento.data) >= '").append(filtroRelatorioAcademicoVO.getAno()).append("' ");
    		if (filtroRelatorioAcademicoVO.getSemestre().equals("1")) {
    			sqlStr.append(" and extract(month from trancamento.data) <= '7' ");
    		} else {
    			sqlStr.append(" and extract(month from trancamento.data) > '7' ");
    		}
    		sqlStr.append(" else matriculaperiodo.ano = '").append(filtroRelatorioAcademicoVO.getAno()).append("' ");
    		sqlStr.append(" and matriculaperiodo.semestre = '").append(filtroRelatorioAcademicoVO.getSemestre()).append("' end ");
    		
//    		sqlStr.append(" and matriculaperiodo.ano = '").append(filtroRelatorioAcademicoVO.getAno()).append("' and matriculaperiodo.semestre = '").append(filtroRelatorioAcademicoVO.getSemestre()).append("' ");
    	}        
    	if(filtroRelatorioAcademicoVO.getFiltrarPorAno()){
    		sqlStr.append(" AND case when trancamento.codigo is not null then ");
    		sqlStr.append(" extract(year from trancamento.data) >= '").append(filtroRelatorioAcademicoVO.getAno()).append("' ");
    		sqlStr.append(" else matriculaperiodo.ano = '").append(filtroRelatorioAcademicoVO.getAno()).append("' end ");
//    		sqlStr.append(" and matriculaperiodo.ano = '").append(filtroRelatorioAcademicoVO.getAno()).append("'  ");
    	}
    	if (ordenarPor.equals("data")) {
    		sqlStr.append(" ORDER BY motivocancelamentotrancamento.nome, unidadeensino, matriculaperiodo.datafechamentomatriculaperiodo, pessoa.nome");    		
    	} else if (ordenarPor.equals("aluno")) {
    		sqlStr.append(" ORDER BY motivocancelamentotrancamento.nome, unidadeensino, pessoa.nome, matriculaperiodo.datafechamentomatriculaperiodo ");
    	} else {
    		sqlStr.append(" ORDER BY motivocancelamentotrancamento.nome, unidadeensino, turma.identificadorturma, pessoa.nome, matriculaperiodo.datafechamentomatriculaperiodo");
    	}
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
    	return montarDadosConsultaRapidaTrancamento(tabelaResultado);
    }
    
    private List<TrancamentoVO> consultarAbandonoCursoPorData(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, MotivoCancelamentoTrancamentoVO motivoCancelamentoTrancamentoVO, Integer unidadeEnsino, String ordenarPor) throws Exception {
    	StringBuilder sqlStr = new StringBuilder();
    	sqlStr.append(" SELECT distinct pessoa.nome AS \"pessoa.nome\", pessoa.telefoneres, pessoa.celular, matricula.matricula, ");
    	sqlStr.append(" case when trancamento.codigo is not null then trancamento.data else matriculaperiodo.datafechamentomatriculaperiodo end as data, ");
    	sqlStr.append(" curso.nome AS \"curso.nome\", trancamento.justificativa, trancamento.descricao, usuario.nome AS \"responsavel.nome\", motivocancelamentotrancamento.nome as motivo, ");
    	sqlStr.append(" unidadeensino.nome AS \"unidadeensino\", matriculaperiodo.datafechamentomatriculaperiodo, turma.identificadorturma as turma ");
    	sqlStr.append(" FROM matricula ");        
    	sqlStr.append(" INNER JOIN matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.situacaomatriculaperiodo = 'AC' ");
    	if(filtroRelatorioAcademicoVO.getFiltrarPorPeriodoData()){
    		sqlStr.append(" and matriculaperiodo.codigo = (select mp.codigo from matriculaperiodo mp ");
    		sqlStr.append(" left join trancamento on trancamento.matriculaperiodo = matriculaperiodo.codigo ");
    		sqlStr.append(" and trancamentorelativoabondonocurso ");
    		sqlStr.append(" WHERE mp.matricula = matricula.matricula ");
    		sqlStr.append(" and case when trancamento.matriculaperiodo is not null then ");
    		sqlStr.append(" trancamento.data >= '").append(Uteis.getDataJDBC(filtroRelatorioAcademicoVO.getDataInicio())).append("' ");
    		sqlStr.append(" and trancamento.data <= '").append(Uteis.getDataJDBC(filtroRelatorioAcademicoVO.getDataTermino())).append("' and trancamentorelativoabondonocurso ");
    		sqlStr.append(" else ");
    		sqlStr.append(" mp.datafechamentomatriculaperiodo::DATE >= '");
    		sqlStr.append(Uteis.getDataJDBC(filtroRelatorioAcademicoVO.getDataInicio()));
    		sqlStr.append("' AND mp.datafechamentomatriculaperiodo::DATE <=  '");
    		sqlStr.append(Uteis.getDataJDBC(filtroRelatorioAcademicoVO.getDataTermino()));
    		sqlStr.append("' end order by (mp.ano || mp.semestre) desc, mp.codigo desc limit 1) ");
    	}
    	        
    	sqlStr.append(" INNER JOIN turma on turma.codigo = matriculaperiodo.turma ");
    	sqlStr.append(" INNER JOIN pessoa on pessoa.codigo = matricula.aluno ");
    	sqlStr.append(" INNER JOIN curso on curso.codigo = matricula.curso ");
    	sqlStr.append(" LEFT JOIN trancamento on trancamento.matricula = matricula.matricula ");
    	sqlStr.append(" left JOIN motivocancelamentotrancamento on motivocancelamentotrancamento.codigo = trancamento.motivocancelamentotrancamento ");
    	sqlStr.append(" LEFT JOIN usuario on usuario.codigo = trancamento.responsavelAutorizacao ");
    	sqlStr.append(" inner JOIN unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
    	sqlStr.append(" WHERE 1=1 ");
    	sqlStr.append(" and trancamentorelativoabondonocurso ");
    	if(unidadeEnsino != null && unidadeEnsino != 0) {
    		sqlStr.append(" AND matricula.unidadeensino = ");
    		sqlStr.append(unidadeEnsino);
    	}
    	if(Uteis.isAtributoPreenchido(motivoCancelamentoTrancamentoVO)){
        	sqlStr.append(" AND trancamento.motivoCancelamentoTrancamento = ").append(motivoCancelamentoTrancamentoVO.getCodigo());
        }
    	if(filtroRelatorioAcademicoVO.getFiltrarPorPeriodoData()){
    		sqlStr.append(" AND case when trancamento.codigo is not null then  ");
        	sqlStr.append(" trancamento.data >= '").append(Uteis.getDataJDBC(filtroRelatorioAcademicoVO.getDataInicio())).append("' ");
        	sqlStr.append(" and trancamento.data <= '").append(Uteis.getDataJDBC(filtroRelatorioAcademicoVO.getDataTermino())).append("' ");
        	sqlStr.append(" else ");
        	sqlStr.append(" matriculaperiodo.datafechamentomatriculaperiodo::DATE >= '").append(Uteis.getDataJDBC(filtroRelatorioAcademicoVO.getDataInicio())).append("' ");
        	sqlStr.append(" and matriculaperiodo.datafechamentomatriculaperiodo::DATE <=  '").append(Uteis.getDataJDBC(filtroRelatorioAcademicoVO.getDataTermino())).append("' ");
        	sqlStr.append(" end ");
    	}
    	if(filtroRelatorioAcademicoVO.getFiltrarPorAnoSemestre()){
    		sqlStr.append(" AND case when trancamento.codigo is not null then ");
    		sqlStr.append(" extract(year from trancamento.data) >= '").append(filtroRelatorioAcademicoVO.getAno()).append("' ");
    		if (filtroRelatorioAcademicoVO.getSemestre().equals("1")) {
    			sqlStr.append(" and extract(month from trancamento.data) <= '7' ");
    		} else {
    			sqlStr.append(" and extract(month from trancamento.data) > '7' ");
    		}
    		sqlStr.append(" else matriculaperiodo.ano = '").append(filtroRelatorioAcademicoVO.getAno()).append("' ");
    		sqlStr.append(" and matriculaperiodo.semestre = '").append(filtroRelatorioAcademicoVO.getSemestre()).append("' end ");
    		
//    		sqlStr.append(" and matriculaperiodo.ano = '").append(filtroRelatorioAcademicoVO.getAno()).append("' and matriculaperiodo.semestre = '").append(filtroRelatorioAcademicoVO.getSemestre()).append("' ");
    	}        
    	if(filtroRelatorioAcademicoVO.getFiltrarPorAno()){
    		sqlStr.append(" AND case when trancamento.codigo is not null then ");
    		sqlStr.append(" extract(year from trancamento.data) >= '").append(filtroRelatorioAcademicoVO.getAno()).append("' ");
    		sqlStr.append(" else matriculaperiodo.ano = '").append(filtroRelatorioAcademicoVO.getAno()).append("' end ");
//    		sqlStr.append(" and matriculaperiodo.ano = '").append(filtroRelatorioAcademicoVO.getAno()).append("'  ");
    	}
    	if (ordenarPor.equals("data")) {
    		sqlStr.append(" ORDER BY motivocancelamentotrancamento.nome, unidadeensino, matriculaperiodo.datafechamentomatriculaperiodo, pessoa.nome");    		
    	} else if (ordenarPor.equals("aluno")) {
    		sqlStr.append(" ORDER BY motivocancelamentotrancamento.nome, unidadeensino, pessoa.nome, matriculaperiodo.datafechamentomatriculaperiodo ");
    	} else {
    		sqlStr.append(" ORDER BY motivocancelamentotrancamento.nome, unidadeensino, turma.identificadorturma, pessoa.nome, matriculaperiodo.datafechamentomatriculaperiodo");
    	}
    	
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
    	return montarDadosConsultaRapidaTrancamento(tabelaResultado);
    }
    
    public List<TrancamentoVO> montarDadosConsultaRapidaTrancamento(SqlRowSet tabelaResultado) throws Exception {
        List<TrancamentoVO> vetResultado = new ArrayList<TrancamentoVO>(0);
        while (tabelaResultado.next()) {
            TrancamentoVO obj = new TrancamentoVO();
            montarDadosTrancamento(obj, tabelaResultado);
            vetResultado.add(obj);
            if (tabelaResultado.getRow() == 0) {
                return vetResultado;
            }
        }
        return vetResultado;
    }

    private void montarDadosTrancamento(TrancamentoVO obj, SqlRowSet dadosSQL) throws Exception {
        // Dados do Funcionário
        obj.getMatricula().getAluno().setNome(dadosSQL.getString("pessoa.nome"));
        obj.getMatricula().setMatricula(dadosSQL.getString("matricula"));
        obj.getMatricula().getAluno().setTelefoneRes(dadosSQL.getString("telefoneres"));
        obj.getMatricula().getAluno().setCelular(dadosSQL.getString("celular"));
        obj.getMatricula().getCurso().setNome(dadosSQL.getString("curso.nome"));
        obj.getMatricula().getUnidadeEnsino().setNome(dadosSQL.getString("unidadeensino"));
        obj.setData(dadosSQL.getDate("data"));
        obj.setJustificativa(dadosSQL.getString("justificativa"));
        obj.getResponsavelAutorizacao().setNome(dadosSQL.getString("responsavel.nome"));
        obj.getMotivoCancelamentoTrancamento().setNome(dadosSQL.getString("motivo"));
        obj.setTurma(dadosSQL.getString("turma"));
        obj.setDescricao(dadosSQL.getString("descricao"));
    }
    

//    private List<TransferenciaEntradaVO> consultarTodasTransferenciasEntradaPorDataInformada(Date datainicio, Date dataFim, Integer unidadeEnsino) throws Exception {
//		List<TransferenciaEntradaVO> listaResultado = getFacadeFactory().getTransferenciaEntradaFacade().consultarTodosTiposPorData(datainicio, dataFim, unidadeEnsino, false,
//				Uteis.NIVELMONTARDADOS_DADOSCONSULTA,null, null);
//		return listaResultado;
//        return null;
//    }
    
    private List<TransferenciaEntradaVO> consultarTodasTransferenciasEntradaPorData(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO,  Integer unidadeEnsino, String tipoTransferenciaEntrada, String ordenarPor) throws Exception {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT distinct pessoa.nome AS \"pessoa.nome\", pessoa.telefoneres, pessoa.celular, matricula.matricula, ");
        sqlStr.append(" curso.nome AS \"curso.nome\", TransferenciaEntrada.data, TransferenciaEntrada.justificativa, TransferenciaEntrada.descricao, usuario.nome AS \"responsavel.nome\", ");
        sqlStr.append(" unidadeensino.nome as unidadeensino, turma.identificadorturma as turma ");
        sqlStr.append(" FROM TransferenciaEntrada ");
        sqlStr.append(" INNER JOIN matriculaperiodo on matriculaperiodo.transferenciaentrada = TransferenciaEntrada.codigo ");
        sqlStr.append(" INNER JOIN turma on turma.codigo = matriculaperiodo.turma ");
        sqlStr.append(" INNER JOIN matricula on matricula.matricula = matriculaperiodo.matricula ");
        sqlStr.append(" INNER JOIN pessoa on pessoa.codigo = matricula.aluno ");
        sqlStr.append(" INNER JOIN curso on curso.codigo = matricula.curso ");
        sqlStr.append(" LEFT JOIN usuario on usuario.codigo = TransferenciaEntrada.responsavelautorizacao ");
        sqlStr.append(" inner JOIN unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
        sqlStr.append(" WHERE 1=1 ");
        if(filtroRelatorioAcademicoVO.getFiltrarPorPeriodoData()){
        	sqlStr.append(" AND TransferenciaEntrada.data::DATE >= '");
        	sqlStr.append(Uteis.getDataJDBC(filtroRelatorioAcademicoVO.getDataInicio()));
        	sqlStr.append("' AND TransferenciaEntrada.data::DATE <= '");
        	sqlStr.append(Uteis.getDataJDBC(filtroRelatorioAcademicoVO.getDataTermino()));
        	sqlStr.append("'");
        }
        if(filtroRelatorioAcademicoVO.getFiltrarPorAnoSemestre()){
        	sqlStr.append(" and extract(year from TransferenciaEntrada.data) >= '").append(filtroRelatorioAcademicoVO.getAno()).append("' ");
        	if (filtroRelatorioAcademicoVO.getSemestre().equals("1")) {
        		sqlStr.append(" and extract(month from TransferenciaEntrada.data) <= '7' ");
        	} else {
        		sqlStr.append(" and extract(month from TransferenciaEntrada.data) > '7' ");
        	}
        } 
        if(filtroRelatorioAcademicoVO.getFiltrarPorAno()){
        	sqlStr.append(" and extract(year from TransferenciaEntrada.data) >= '").append(filtroRelatorioAcademicoVO.getAno()).append("' ");
        } 
        if(unidadeEnsino != null && unidadeEnsino != 0) {
            sqlStr.append(" AND matricula.unidadeensino = ");
            sqlStr.append(unidadeEnsino);
        }
        if(!tipoTransferenciaEntrada.equals("")) {
            sqlStr.append(" and tipoTransferenciaEntrada = '");
            sqlStr.append(tipoTransferenciaEntrada);
            sqlStr.append("'");
        }
        if (ordenarPor.equals("data")) {
        	sqlStr.append(" ORDER BY TransferenciaEntrada.data, pessoa.nome");
    	} else if (ordenarPor.equals("aluno")) {
    		sqlStr.append(" ORDER BY pessoa.nome, TransferenciaEntrada.data");
    	} else {
    		sqlStr.append(" ORDER BY turma.identificadorturma, pessoa.nome, TransferenciaEntrada.data");
    	}
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapidaTransferenciaEntrada(tabelaResultado);
    }
    
    public List<TransferenciaEntradaVO> montarDadosConsultaRapidaTransferenciaEntrada(SqlRowSet tabelaResultado) throws Exception {
        List<TransferenciaEntradaVO> vetResultado = new ArrayList<TransferenciaEntradaVO>(0);
        while (tabelaResultado.next()) {
            TransferenciaEntradaVO obj = new TransferenciaEntradaVO();
            montarDadosTransferenciaEntrada(obj, tabelaResultado);
            vetResultado.add(obj);
            if (tabelaResultado.getRow() == 0) {
                return vetResultado;
            }
        }
        return vetResultado;
    }

    private void montarDadosTransferenciaEntrada(TransferenciaEntradaVO obj, SqlRowSet dadosSQL) throws Exception {
        // Dados do Funcionário
        obj.getMatricula().getAluno().setNome(dadosSQL.getString("pessoa.nome"));
        obj.getMatricula().setMatricula(dadosSQL.getString("matricula"));
        obj.getMatricula().getAluno().setTelefoneRes(dadosSQL.getString("telefoneres"));
        obj.getMatricula().getAluno().setCelular(dadosSQL.getString("celular"));
        obj.getMatricula().getCurso().setNome(dadosSQL.getString("curso.nome"));
        obj.getMatricula().getUnidadeEnsino().setNome(dadosSQL.getString("unidadeensino"));
        obj.setData(dadosSQL.getDate("data"));
        obj.setJustificativa(dadosSQL.getString("justificativa"));
        obj.getResponsavelAutorizacao().setNome(dadosSQL.getString("responsavel.nome"));
        obj.getTurma().setIdentificadorTurma(dadosSQL.getString("turma"));

    }

//    private List<TransferenciaEntradaVO> consultarTransferenciasEntradaPorDataInformada(Date datainicio, Date dataFim, Integer unidadeEnsino, boolean matriculado,
//            TipoTransferenciaEntrada tipoTransferenciaEntrada) throws Exception {
////		List<TransferenciaEntradaVO> listaResultado = getFacadeFactory().getTransferenciaEntradaFacade().consultarPorDataUnidadeEnsino(datainicio, dataFim, unidadeEnsino, matriculado,
////				tipoTransferenciaEntrada, false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, null,null);
////		return listaResultado;
//        return null;
//    }

//    private List<TransferenciaSaidaVO> consultarTransferenciasSaidaPorDataInformada(Date datainicio, Date dataFim, Integer unidadeEnsino) throws Exception {
////		List<TransferenciaSaidaVO> listaResultado = getFacadeFactory().getTransferenciaSaidaFacade().consultarPorDataUnidadeEnsino(datainicio, dataFim, unidadeEnsino, false,
////				Uteis.NIVELMONTARDADOS_DADOSCONSULTA, null, null);
////		return listaResultado;
//        return null;
//    }
    
     private List<TransferenciaSaidaVO> consultarTodasTransferenciasSaidaPorData(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, Integer unidadeEnsino, String ordenarPor) throws Exception {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT distinct pessoa.nome AS \"pessoa.nome\", pessoa.telefoneres, pessoa.celular, matricula.matricula, ");
        sqlStr.append(" curso.nome AS \"curso.nome\", matriculaperiodo.datafechamentomatriculaperiodo as data, TransferenciaSaida.justiticativa, TransferenciaSaida.descricao, usuario.nome AS \"responsavel.nome\", ");
        sqlStr.append(" unidadeensino.nome as unidadeensino, turma.identificadorturma as turma");
        sqlStr.append(" FROM matricula ");        
        sqlStr.append(" INNER JOIN matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.situacaomatriculaperiodo = 'TS' ");
        if(filtroRelatorioAcademicoVO.getFiltrarPorPeriodoData()){
        	sqlStr.append(" and matriculaperiodo.codigo = (select codigo from matriculaperiodo mp  WHERE mp.matricula = matricula.matricula and mp.datafechamentomatriculaperiodo::DATE >= '");
        	sqlStr.append(Uteis.getDataJDBC(filtroRelatorioAcademicoVO.getDataInicio()));
        	sqlStr.append("' AND mp.datafechamentomatriculaperiodo::DATE <=  '");
        	sqlStr.append(Uteis.getDataJDBC(filtroRelatorioAcademicoVO.getDataTermino()));
        	sqlStr.append("' order by (mp.ano || mp.semestre) desc, mp.codigo desc  limit 1) ");
        }
                
        sqlStr.append(" INNER JOIN turma on turma.codigo = matriculaperiodo.turma ");
        sqlStr.append(" INNER JOIN pessoa on pessoa.codigo = matricula.aluno ");
        sqlStr.append(" INNER JOIN curso on curso.codigo = matricula.curso ");
        sqlStr.append(" LEFT JOIN TransferenciaSaida on matricula.matricula = TransferenciaSaida.matricula  and TransferenciaSaida.codigo = (select max(codigo) from TransferenciaSaida ts where matricula.matricula = ts.matricula and ts.data::DATE <= matriculaperiodo.datafechamentomatriculaperiodo::DATE  )");
        sqlStr.append(" LEFT JOIN usuario on usuario.codigo = TransferenciaSaida.responsavelautorizacao ");
        sqlStr.append(" inner JOIN unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
        if(unidadeEnsino != null && unidadeEnsino != 0) {
            sqlStr.append(" WHERE matricula.unidadeensino = ");
            sqlStr.append(unidadeEnsino);
        }
        if(filtroRelatorioAcademicoVO.getFiltrarPorPeriodoData()){
        	sqlStr.append(" and case when TransferenciaSaida.codigo is not null then ");
            sqlStr.append(" TransferenciaSaida.data >= '").append(Uteis.getDataJDBC(filtroRelatorioAcademicoVO.getDataInicio())).append("' ");
            sqlStr.append(" and matriculaperiodo.datafechamentomatriculaperiodo >= '").append(Uteis.getDataJDBC(filtroRelatorioAcademicoVO.getDataTermino())).append("' ");
            sqlStr.append(" end ");
        }
        if(filtroRelatorioAcademicoVO.getFiltrarPorAnoSemestre()){
    		sqlStr.append(" AND case when TransferenciaSaida.codigo is not null then ");
    		sqlStr.append(" extract(year from TransferenciaSaida.data) >= '").append(filtroRelatorioAcademicoVO.getAno()).append("' ");
    		if (filtroRelatorioAcademicoVO.getSemestre().equals("1")) {
    			sqlStr.append(" and extract(month from TransferenciaSaida.data) <= '7' ");
    		} else {
    			sqlStr.append(" and extract(month from TransferenciaSaida.data) > '7' ");
    		}
    		sqlStr.append(" else matriculaperiodo.ano = '").append(filtroRelatorioAcademicoVO.getAno()).append("' ");
    		sqlStr.append(" and matriculaperiodo.semestre = '").append(filtroRelatorioAcademicoVO.getSemestre()).append("' end ");
    		
    	}        
    	if(filtroRelatorioAcademicoVO.getFiltrarPorAno()){
    		sqlStr.append(" AND case when TransferenciaSaida.codigo is not null then ");
    		sqlStr.append(" extract(year from TransferenciaSaida.data) >= '").append(filtroRelatorioAcademicoVO.getAno()).append("' ");
    		sqlStr.append(" else matriculaperiodo.ano = '").append(filtroRelatorioAcademicoVO.getAno()).append("' end ");
    	}
    	if (ordenarPor.equals("data")) {
    		sqlStr.append(" ORDER BY matriculaperiodo.datafechamentomatriculaperiodo, pessoa.nome");    		
    	} else if (ordenarPor.equals("aluno")) {
    		sqlStr.append(" ORDER BY pessoa.nome, matriculaperiodo.datafechamentomatriculaperiodo");
    	} else {
    		sqlStr.append(" ORDER BY turma.identificadorturma, pessoa.nome, matriculaperiodo.datafechamentomatriculaperiodo");
    	}
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapidaTransferenciaSaida(tabelaResultado);
    }
     
     private List<TransferenciaEntradaVO> consultarTodasTransferenciasInternaPorData(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, Integer unidadeEnsino, String ordenarPor) throws Exception {
         StringBuilder sqlStr = new StringBuilder();
         sqlStr.append("SELECT distinct pessoa.nome AS \"pessoa.nome\", pessoa.telefoneres, pessoa.celular, matricula.matricula, ");
         sqlStr.append(" curso.nome AS \"curso.nome\", matriculaperiodo.datafechamentomatriculaperiodo as data, transferenciaentrada.justificativa, usuario.nome AS \"responsavel.nome\", ");
         sqlStr.append(" unidadeensino.nome as unidadeensino, turma.identificadorturma as turma ");
         sqlStr.append(" FROM matricula ");        
         sqlStr.append(" INNER JOIN matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.situacaomatriculaperiodo = 'TI' ");
         if(filtroRelatorioAcademicoVO.getFiltrarPorPeriodoData()){
         	sqlStr.append(" and matriculaperiodo.codigo = (select codigo from matriculaperiodo mp  WHERE mp.matricula = matricula.matricula and mp.datafechamentomatriculaperiodo::DATE >= '");
         	sqlStr.append(Uteis.getDataJDBC(filtroRelatorioAcademicoVO.getDataInicio()));
         	sqlStr.append("' AND mp.datafechamentomatriculaperiodo::DATE <=  '");
         	sqlStr.append(Uteis.getDataJDBC(filtroRelatorioAcademicoVO.getDataTermino()));
         	sqlStr.append("' order by (mp.ano || mp.semestre) desc, mp.codigo desc limit 1) ");
         }
         sqlStr.append(" INNER JOIN turma on turma.codigo = matriculaperiodo.turma ");
         sqlStr.append(" INNER JOIN pessoa on pessoa.codigo = matricula.aluno ");
         sqlStr.append(" INNER JOIN curso on curso.codigo = matricula.curso ");
         sqlStr.append(" LEFT JOIN transferenciaentrada ON transferenciaentrada.matricula = matricula.matricula ");
         sqlStr.append(" and transferenciaentrada.tipotransferenciaentrada = 'IN' ");
         sqlStr.append(" LEFT JOIN usuario on usuario.codigo = transferenciaentrada.responsavelautorizacao ");
         sqlStr.append(" inner JOIN unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
         sqlStr.append(" where 1=1 ");
         if(unidadeEnsino != null && unidadeEnsino != 0) {
             sqlStr.append(" AND matricula.unidadeensino = ");
             sqlStr.append(unidadeEnsino);
         }
         if(filtroRelatorioAcademicoVO.getFiltrarPorPeriodoData()){
        	 sqlStr.append(" and case when transferenciaentrada.codigo is not null then  ");
        	 sqlStr.append(" transferenciaentrada.data >= '").append(Uteis.getDataJDBC(filtroRelatorioAcademicoVO.getDataInicio())).append("' ");
        	 sqlStr.append(" and transferenciaentrada.data <= '").append(Uteis.getDataJDBC(filtroRelatorioAcademicoVO.getDataTermino())).append("' ");
        	 sqlStr.append(" else matriculaperiodo.datafechamentomatriculaperiodo >= '").append(Uteis.getDataJDBC(filtroRelatorioAcademicoVO.getDataInicio())).append("' ");
        	 sqlStr.append(" and matriculaperiodo.datafechamentomatriculaperiodo >= '").append(Uteis.getDataJDBC(filtroRelatorioAcademicoVO.getDataTermino())).append("' ");
        	 sqlStr.append(" end ");
         }
         if(filtroRelatorioAcademicoVO.getFiltrarPorAnoSemestre()){
         	sqlStr.append(" and extract(year from TransferenciaEntrada.data) >= '").append(filtroRelatorioAcademicoVO.getAno()).append("' ");
         	if (filtroRelatorioAcademicoVO.getSemestre().equals("1")) {
         		sqlStr.append(" and extract(month from TransferenciaEntrada.data) <= '7' ");
         	} else {
         		sqlStr.append(" and extract(month from TransferenciaEntrada.data) > '7' ");
         	}
         } 
         if(filtroRelatorioAcademicoVO.getFiltrarPorAno()){
         	sqlStr.append(" and extract(year from TransferenciaEntrada.data) >= '").append(filtroRelatorioAcademicoVO.getAno()).append("' ");
         }         
         if (ordenarPor.equals("data")) {
        	 sqlStr.append(" ORDER BY matriculaperiodo.datafechamentomatriculaperiodo, pessoa.nome");
     	 } else if (ordenarPor.equals("aluno")) {
     		 sqlStr.append(" ORDER BY pessoa.nome, matriculaperiodo.datafechamentomatriculaperiodo");
     	 } else {
     		 sqlStr.append(" ORDER BY turma.identificadorturma, pessoa.nome, matriculaperiodo.datafechamentomatriculaperiodo");
     	 }
         SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
         return montarDadosConsultaRapidaTransferenciaEntrada(tabelaResultado);
     }
    
    public List<TransferenciaSaidaVO> montarDadosConsultaRapidaTransferenciaSaida(SqlRowSet tabelaResultado) throws Exception {
        List<TransferenciaSaidaVO> vetResultado = new ArrayList<TransferenciaSaidaVO>(0);
        while (tabelaResultado.next()) {
            TransferenciaSaidaVO obj = new TransferenciaSaidaVO();
            montarDadosTransferenciaSaida(obj, tabelaResultado);
            vetResultado.add(obj);
            if (tabelaResultado.getRow() == 0) {
                return vetResultado;
            }
        }
        return vetResultado;
    }

    private void montarDadosTransferenciaSaida(TransferenciaSaidaVO obj, SqlRowSet dadosSQL) throws Exception {
        // Dados do Funcionário
        obj.getMatricula().getAluno().setNome(dadosSQL.getString("pessoa.nome"));
        obj.getMatricula().setMatricula(dadosSQL.getString("matricula"));
        obj.getMatricula().getAluno().setTelefoneRes(dadosSQL.getString("telefoneres"));
        obj.getMatricula().getAluno().setCelular(dadosSQL.getString("celular"));
        obj.getMatricula().getCurso().setNome(dadosSQL.getString("curso.nome"));
        obj.getMatricula().getUnidadeEnsino().setNome(dadosSQL.getString("unidadeensino"));
        obj.setData(dadosSQL.getDate("data"));
        obj.setJustiticativa(dadosSQL.getString("justiticativa"));
        obj.setDescricao(dadosSQL.getString("descricao"));
        obj.getResponsavelAutorizacao().setNome(dadosSQL.getString("responsavel.nome"));
        obj.setTurma(dadosSQL.getString("turma"));
    }

    public static String getDesignIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
    }

    public static String getCaminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
    }

    public static String getIdEntidade() {
        return ("OcorrenciasAlunosRel");
    }
    
    public static String getDesignIReportRelatorioExcel() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadeExcel() + ".jrxml");
    }
    
    public static String getIdEntidadeExcel() {
        return ("OcorrenciasAlunosExcelRel");
    }
    
    private List<OcorrenciasAlunosVO> consultarFormadosPorData(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, Integer unidadeEnsino, String ordenarPor) throws Exception {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT distinct matricula.matricula, pessoa.nome AS \"pessoa.nome\", pessoa.telefoneres, pessoa.celular, ");
        sqlStr.append(" matricula.dataAtualizacaoMatriculaFormada as data, ");
        sqlStr.append(" curso.nome AS \"curso.nome\", usuario.nome AS \"responsavel.nome\", ");
        sqlStr.append(" unidadeensino.nome as unidadeensino, turma.identificadorturma as turma ");
        sqlStr.append(" FROM matricula ");
        sqlStr.append(" INNER JOIN matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
        sqlStr.append(" INNER JOIN turma on turma.codigo = matriculaperiodo.turma ");
        sqlStr.append(" INNER JOIN pessoa on pessoa.codigo = matricula.aluno ");
        sqlStr.append(" INNER JOIN curso on curso.codigo = matricula.curso ");
        sqlStr.append(" left JOIN usuario on usuario.codigo = matricula.responsavelAtualizacaoMatriculaFormada ");
        sqlStr.append(" inner JOIN unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
        sqlStr.append(" WHERE matricula.situacao = 'FO' ");
        
        if(unidadeEnsino != null && unidadeEnsino != 0) {
            sqlStr.append(" AND matricula.unidadeensino = ");
            sqlStr.append(unidadeEnsino);
        }
        if(filtroRelatorioAcademicoVO.getFiltrarPorPeriodoData()){
        	sqlStr.append(" and matricula.dataAtualizacaoMatriculaFormada >= '").append(Uteis.getDataJDBC(filtroRelatorioAcademicoVO.getDataInicio())).append("' ");
        	sqlStr.append(" and matricula.dataAtualizacaoMatriculaFormada <= '").append(Uteis.getDataJDBC(filtroRelatorioAcademicoVO.getDataTermino())).append("' ");
        }
        if(filtroRelatorioAcademicoVO.getFiltrarPorAnoSemestre()){
        	sqlStr.append(" and extract(year from matricula.dataAtualizacaoMatriculaFormada) >= '").append(filtroRelatorioAcademicoVO.getAno()).append("' ");
    		if (filtroRelatorioAcademicoVO.getSemestre().equals("1")) {
    			sqlStr.append(" and extract(month from matricula.dataAtualizacaoMatriculaFormada) <= '7' ");
    		} else {
    			sqlStr.append(" and extract(month from matricula.dataAtualizacaoMatriculaFormada) > '7' ");
    		}
    	}        
    	if(filtroRelatorioAcademicoVO.getFiltrarPorAno()){
    		sqlStr.append(" and extract(year from matricula.dataAtualizacaoMatriculaFormada) >= '").append(filtroRelatorioAcademicoVO.getAno()).append("' ");
    	}
    	if (ordenarPor.equals("data")) {
    		sqlStr.append(" ORDER BY matricula.dataAtualizacaoMatriculaFormada, pessoa.nome");    		
    	} else if (ordenarPor.equals("aluno")) {
    		sqlStr.append(" ORDER BY pessoa.nome, matricula.dataAtualizacaoMatriculaFormada");
    	} else {
    		sqlStr.append(" ORDER BY turma.identificadorturma, pessoa.nome, matricula.dataAtualizacaoMatriculaFormada");
    	}
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapidaFormados(tabelaResultado);
    }
    
    public List<OcorrenciasAlunosVO> montarDadosConsultaRapidaFormados(SqlRowSet tabelaResultado) throws Exception {
        List<OcorrenciasAlunosVO> vetResultado = new ArrayList<OcorrenciasAlunosVO>(0);
        while (tabelaResultado.next()) {
        	OcorrenciasAlunosVO obj = new OcorrenciasAlunosVO();
            montarDadosFormados(obj, tabelaResultado);
            vetResultado.add(obj);
            if (tabelaResultado.getRow() == 0) {
                return vetResultado;
            }
        }
        return vetResultado;
    }

    private void montarDadosFormados(OcorrenciasAlunosVO obj, SqlRowSet dadosSQL) throws Exception {
        obj.getMatricula().getAluno().setNome(dadosSQL.getString("pessoa.nome"));
        obj.getMatricula().setMatricula(dadosSQL.getString("matricula"));
        obj.getMatricula().getAluno().setTelefoneRes(dadosSQL.getString("telefoneres"));
        obj.getMatricula().getAluno().setCelular(dadosSQL.getString("celular"));
        obj.getMatricula().getCurso().setNome(dadosSQL.getString("curso.nome"));
        obj.getMatricula().getUnidadeEnsino().setNome(dadosSQL.getString("unidadeensino"));
        obj.setData(dadosSQL.getDate("data"));
        obj.getResponsavelAutorizacao().setNome(dadosSQL.getString("responsavel.nome"));
        obj.setTurma(dadosSQL.getString("turma"));
    }

	@Override
	public void validarDados(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) throws Exception {
		if (filtroRelatorioAcademicoVO.getFiltrarPorPeriodoData()) {
			if (filtroRelatorioAcademicoVO.getDataInicio() == null) {
				throw new Exception("A DATA DE INÍCIO deve ser informado!");
			}
			if (filtroRelatorioAcademicoVO.getDataTermino() == null) {
				throw new Exception("A DATA FINAL deve ser informado!");
			}
			if ((filtroRelatorioAcademicoVO.getDataInicio().compareTo(filtroRelatorioAcademicoVO.getDataTermino()) >= 1)) {
				throw new Exception("A DATA DE INÍCIO não pode ser maior que a DATA FINAL!");
			}
		}
		if (filtroRelatorioAcademicoVO.getFiltrarPorAnoSemestre()) {
			if (filtroRelatorioAcademicoVO.getAno().trim().isEmpty()) {
				throw new Exception("A ANO deve ser informado!");
			}
			if (filtroRelatorioAcademicoVO.getSemestre().trim().isEmpty()) {
				throw new Exception("A SEMESTRE deve ser informado!");
			}
		}
		if (filtroRelatorioAcademicoVO.getFiltrarPorAno()) {
			if (filtroRelatorioAcademicoVO.getAno().trim().isEmpty()) {
				throw new Exception("A ANO deve ser informado!");
			}			
		}
		
	}
}
