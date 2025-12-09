package negocio.facade.jdbc.secretaria;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.enumeradores.ClassificacaoDisciplinaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.secretaria.CalendarioAgrupamentoDisciplinaVO;
import negocio.comuns.secretaria.CalendarioAgrupamentoTccVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.secretaria.CalendarioAgrupamentoDisciplinaInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class CalendarioAgrupamentoDisciplina extends ControleAcesso implements CalendarioAgrupamentoDisciplinaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1070811955995803897L;
	private static final String TABELA =  "calendarioAgrupamentoDisciplina";

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirCalendarioAgrupamentoDisciplinaVOs(CalendarioAgrupamentoTccVO calendarioAgrupamentoTccVO, UsuarioVO usuarioVO) throws Exception {		
		for(CalendarioAgrupamentoDisciplinaVO obj: calendarioAgrupamentoTccVO.getCalendarioAgrupamentoDisciplinaVOs()) {
			if(obj.getSelecionado()) {
				obj.setCalendarioAgrupamentoVO(calendarioAgrupamentoTccVO);
				incluir(obj, CalendarioAgrupamentoDisciplina.TABELA, new AtributoPersistencia().add("disciplina", obj.getDisciplinaVO()).add("calendarioAgrupamento", calendarioAgrupamentoTccVO), usuarioVO);
			}
		}
				
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarCalendarioAgrupamentoDisciplinaVOs(CalendarioAgrupamentoTccVO calendarioAgrupamentoTccVO, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("delete from calendarioAgrupamentoDisciplina where calendarioAgrupamento = ? and disciplina not in (0");	
		for(CalendarioAgrupamentoDisciplinaVO obj: calendarioAgrupamentoTccVO.getCalendarioAgrupamentoDisciplinaVOs()) {
			if(obj.getSelecionado()) {
				obj.setCalendarioAgrupamentoVO(calendarioAgrupamentoTccVO);
				alterar(obj, CalendarioAgrupamentoDisciplina.TABELA, new AtributoPersistencia().add("disciplina", obj.getDisciplinaVO()).add("calendarioAgrupamento", calendarioAgrupamentoTccVO), new AtributoPersistencia().add("disciplina", obj.getDisciplinaVO()).add("calendarioAgrupamento", calendarioAgrupamentoTccVO), usuarioVO);				
				sql.append(", ").append(obj.getDisciplinaVO().getCodigo());
			}
		}			
		sql.append(") ");
		getConexao().getJdbcTemplate().update(sql.toString(), calendarioAgrupamentoTccVO.getCodigo());
	}
	
	public StringBuilder getSqlPadrao() {
		StringBuilder sql = new StringBuilder("select calendarioAgrupamentoDisciplina.codigo, calendarioAgrupamentoDisciplina.disciplina, calendarioAgrupamentoDisciplina.calendarioAgrupamento, disciplina.abreviatura as disciplina_abreviatura, disciplina.nome as disciplina_nome, disciplina.classificacaoDisciplina as disciplina_classificacaoDisciplina ");
		sql.append(" from calendarioAgrupamentoDisciplina ");
		sql.append(" inner join disciplina on disciplina.codigo = calendarioAgrupamentoDisciplina.disciplina ");
		return sql;
	}

	@Override
	public List<CalendarioAgrupamentoDisciplinaVO> consultarPorCalendarioAgrupamento(Integer calendarioAgrupamento, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = getSqlPadrao();
		sql.append(" where calendarioAgrupamentoDisciplina.calendarioAgrupamento = ? order by disciplina.abreviatura, disciplina.nome ");
		SqlRowSet rs  = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), calendarioAgrupamento);
		return montarDadosConsulta(rs);
	}

	private List<CalendarioAgrupamentoDisciplinaVO> montarDadosConsulta(SqlRowSet rs) throws Exception{
		List<CalendarioAgrupamentoDisciplinaVO> agrupamentoDisciplinaVOs = new ArrayList<CalendarioAgrupamentoDisciplinaVO>(0);
		while(rs.next()) {
			agrupamentoDisciplinaVOs.add(montarDados(rs));
		}
		return agrupamentoDisciplinaVOs;
	}
	
	private CalendarioAgrupamentoDisciplinaVO montarDados(SqlRowSet rs) throws Exception{
		CalendarioAgrupamentoDisciplinaVO agrupamentoDisciplinaVO = new CalendarioAgrupamentoDisciplinaVO();
		agrupamentoDisciplinaVO.setNovoObj(rs.getInt("codigo") == 0);
		agrupamentoDisciplinaVO.setSelecionado(rs.getInt("codigo") != 0);
		agrupamentoDisciplinaVO.getCalendarioAgrupamentoVO().setCodigo(rs.getInt("calendarioAgrupamento"));
		agrupamentoDisciplinaVO.getDisciplinaVO().setCodigo(rs.getInt("disciplina"));
		agrupamentoDisciplinaVO.getDisciplinaVO().setAbreviatura(rs.getString("disciplina_abreviatura"));
		agrupamentoDisciplinaVO.getDisciplinaVO().setNome(rs.getString("disciplina_nome"));
		agrupamentoDisciplinaVO.getDisciplinaVO().setClassificacaoDisciplina(ClassificacaoDisciplinaEnum.valueOf(rs.getString("disciplina_classificacaoDisciplina")));
		return agrupamentoDisciplinaVO;
	}
	
	@Override
	public List<CalendarioAgrupamentoDisciplinaVO> consultarPorDisciplinaNaoSelecionadaPorCalendarioAgrupamento(CalendarioAgrupamentoTccVO calendarioAgrupamentoTccVO, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("select 0 as codigo, disciplina.codigo as disciplina, 0 as calendarioAgrupamento, disciplina.abreviatura as disciplina_abreviatura, disciplina.nome as disciplina_nome, disciplina.classificacaoDisciplina as disciplina_classificacaoDisciplina ");
		sql.append(" from disciplina where disciplina.classificacaoDisciplina = ? and disciplina.codigo not in (select calendarioAgrupamentoDisciplina.disciplina ");
		sql.append(" from calendarioAgrupamentoDisciplina ");
		sql.append(" inner join disciplina on disciplina.codigo = calendarioAgrupamentoDisciplina.disciplina ");
		sql.append(" where calendarioAgrupamentoDisciplina.calendarioAgrupamento = ? ) ");
		if(calendarioAgrupamentoTccVO.getClassificacaoAgrupamento().isTcc()) {
			sql.append(" and exists (select gradecurricular.codigo from gradecurricular where gradecurricular.disciplinapadraotcc =  disciplina.codigo limit 1)  ");
		}
		sql.append(" order by  disciplina.abreviatura, disciplina.nome ");
		SqlRowSet rs  = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), calendarioAgrupamentoTccVO.getClassificacaoAgrupamento().name(), calendarioAgrupamentoTccVO.getCodigo());
		return montarDadosConsulta(rs);		
	}

	
}
