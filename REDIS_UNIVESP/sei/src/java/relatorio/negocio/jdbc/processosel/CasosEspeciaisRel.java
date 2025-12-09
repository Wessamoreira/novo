package relatorio.negocio.jdbc.processosel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.processosel.ItemProcSeletivoDataProvaVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.utilitarias.ConsistirException;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.processosel.CasosEspeciaisRelVO;
import relatorio.negocio.comuns.processosel.ProcessoSeletivoInscricoes_UnidadeEnsino_InscricaoRelVO;
import relatorio.negocio.interfaces.processosel.CasosEspeciaisRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class CasosEspeciaisRel extends SuperRelatorio implements CasosEspeciaisRelInterfaceFacade {
	
	private static final long serialVersionUID = -7114578099885982556L;
	protected static String idEntidade;

    public CasosEspeciaisRel() {
        setIdEntidade("CasosEspeciaisRel");
    }
    
    public List<CasosEspeciaisRelVO> emitirRelatorio(ProcSeletivoVO procSeletivoVO, Integer unidadeEnsino, Integer unidadeEnsinoCurso, Boolean canhoto, Boolean gravida, Boolean necessidadeEspecial, ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO, Integer sala) throws Exception {
    	validarDados(procSeletivoVO, canhoto, gravida, necessidadeEspecial);
    	StringBuilder sql = new StringBuilder();
    	sql.append(" select Pessoa.nome as nome, Pessoa.necessidadesespeciais, Pessoa.canhoto, Pessoa.gravida, curso.nome as curso, ItemProcSeletivoDataProva.dataProva as dataProva, sala.sala ");
    	sql.append(" from Inscricao ");
    	sql.append(" left join SalaLocalAula sala on sala.codigo = inscricao.sala ");
    	sql.append(" inner join pessoa on pessoa.codigo = inscricao.candidato ");
    	sql.append(" inner join unidadeensinocurso on unidadeensinocurso.codigo = inscricao.cursoopcao1 ");
    	sql.append(" inner join curso on curso.codigo = unidadeensinocurso.curso ");
    	sql.append(" inner join procseletivo on procseletivo.codigo = Inscricao.procseletivo ");
    	if (unidadeEnsino != null && !unidadeEnsino.equals(0)) {
    		sql.append(" inner join unidadeensino on unidadeensino.codigo = inscricao.unidadeensino ");
    	}    	
    	sql.append(" inner join ItemProcSeletivoDataProva on ItemProcSeletivoDataProva.codigo = inscricao.itemProcessoSeletivoDataProva ");
    	if(itemProcSeletivoDataProvaVO != null && itemProcSeletivoDataProvaVO.getCodigo() != null && itemProcSeletivoDataProvaVO.getCodigo()>0){
        	sql.append(" and ItemProcSeletivoDataProva.codigo = ").append(itemProcSeletivoDataProvaVO.getCodigo());
        }
    	sql.append(" where ProcSeletivo.codigo = ").append(procSeletivoVO.getCodigo());
    	if(sala != null && sala>=0){
         	if(sala == 0){
         		sql.append(" and Inscricao.sala is null ");
         	}else{
         		sql.append(" and sala.codigo = ").append(sala);
         	}
         }
    	if (unidadeEnsino != null && !unidadeEnsino.equals(0)) {
            sql.append(" and UnidadeEnsino.codigo = " + unidadeEnsino);
        }
        if (unidadeEnsinoCurso != null && !unidadeEnsinoCurso.equals(0)) {
            sql.append(" and UnidadeEnsinoCurso.codigo = " + unidadeEnsinoCurso);
        }
        if (canhoto != null && canhoto == true) {
        	sql.append(" and (Pessoa.canhoto = true ");
        }
        if (gravida != null && gravida == true) {
        	if (canhoto) {
        		sql.append(" or Pessoa.gravida = true ");
        	} else {
        		sql.append(" and Pessoa.gravida = true ");
        	}
        } else if (canhoto) {
        	sql.append(" ) ");
        }
        if (necessidadeEspecial != null && necessidadeEspecial == true) {
        	if (gravida) {
        		sql.append(" or Pessoa.portadornecessidadeespecial = true )");
        	} else {
        		sql.append(" and Pessoa.portadornecessidadeespecial = true ");
        	}
        } else if (gravida && canhoto) {
        	sql.append(" ) "); 
        }
    	sql.append(" order by nome, ItemProcSeletivoDataProva.dataProva ");
    			  
    	return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
    }
    
    private List<CasosEspeciaisRelVO> montarDadosConsulta(SqlRowSet resultadoConsulta) throws Exception {
        List<CasosEspeciaisRelVO> listaConsulta = new ArrayList<CasosEspeciaisRelVO>(0);
        while (resultadoConsulta.next()) {
        	CasosEspeciaisRelVO obj = new CasosEspeciaisRelVO();
        	obj.setCandidato(resultadoConsulta.getString("nome"));
        	obj.setNecessidadesEspeciais(resultadoConsulta.getString("necessidadesespeciais"));
        	if (!obj.getNecessidadesEspeciais().equals("")) {
        		obj.setNecessidadeEspecial(true);
        	}
        	if (resultadoConsulta.getBoolean("canhoto")) {
        		if (obj.getNecessidadesEspeciais().equals("")) {
        			obj.setNecessidadesEspeciais("Canhoto");
        		} else {
        			obj.setNecessidadesEspeciais(obj.getNecessidadesEspeciais() + ", Canhoto");
        		}
        		obj.setCanhoto(true);
        	}
        	if (resultadoConsulta.getBoolean("gravida")) {
        		if (obj.getNecessidadesEspeciais().equals("")) {
        			obj.setNecessidadesEspeciais("Gravida");
        		} else {
        			obj.setNecessidadesEspeciais(obj.getNecessidadesEspeciais() + ", Gravida");
        		}
        		obj.setGravida(true);
        	}
        	obj.setCurso(resultadoConsulta.getString("curso"));
        	obj.setDataProva(resultadoConsulta.getDate("dataProva"));
        	obj.setSala(resultadoConsulta.getString("sala"));
            listaConsulta.add(obj);
        }
        return listaConsulta;
    }
    
    public void validarDados(ProcSeletivoVO obj, Boolean canhoto, Boolean gravida, Boolean necessidadeEspecial) throws ConsistirException {
        if (obj == null || obj.getCodigo() == 0) {
            throw new ConsistirException("O campo Processo Seletivo deve ser informado.");
        }
       if (!canhoto && !gravida && !necessidadeEspecial) {
    	   throw new ConsistirException("É obrigatório informar ao menos um tipo de necessidade especial.");
       }
    }
    
    public static String getIdEntidade() {
        return CasosEspeciaisRel.idEntidade;
    }
    
    public void setIdEntidade(String idEntidade) {
    	CasosEspeciaisRel.idEntidade = idEntidade;
    }
    
    public String getDesignIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator + "CasosEspeciaisRel.jrxml");
    }
    
    public String getCaminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator);
    }
    
}
