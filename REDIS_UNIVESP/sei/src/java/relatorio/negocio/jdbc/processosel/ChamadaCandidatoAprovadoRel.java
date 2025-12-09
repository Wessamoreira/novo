package relatorio.negocio.jdbc.processosel;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.ProcSeletivoUnidadeEnsinoVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.processosel.ProcSeletivoUnidadeEnsino;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.processosel.ChamadaCandidatoAprovadoRelVO;
import relatorio.negocio.interfaces.processosel.ChamadaCandidatoAprovadoRelInterfaceFacade;

@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class ChamadaCandidatoAprovadoRel extends ControleAcesso implements ChamadaCandidatoAprovadoRelInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public ChamadaCandidatoAprovadoRel() {

	}
	
	public void validarDados(Integer curso, UsuarioVO usuarioVO) throws Exception {
		if (curso == null || curso.equals(0)) {
			throw new Exception("O campo CURSO deve ser informado.");
		}
	}


	public List<ChamadaCandidatoAprovadoRelVO> consultarCandidatosAprovados(ProcSeletivoVO procSeletivoVO, Integer itemProcSeletivoDataProvaVO, Integer curso, Integer numeroVagas, Integer nrCandidatosMatriculados,  Integer qtdeCandidatoChamar, Integer quantidadeCasaDecimalConsiderarNotaProcessoSeletivo, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT * FROM ( SELECT COALESCE(inscricao.classificacao, 0) classificacao, ");
        sb.append(" inscricao.codigo AS numeroInscricao, pessoa.codigo AS \"pessoa.codigo\", pessoa.nome AS \"pessoa.nome\", pessoa.email, pessoa.telefoneres, pessoa.celular, pessoa.cpf, pessoa.datanasc, medianotasprocseletivo, notaredacao, procseletivo.regimeaprovacao, inscricao.itemprocessoseletivodataprova as codigoDataProva , inscricao.candidatoconvocadomatricula  ");
		sb.append(" FROM resultadoprocessoseletivo ");
		sb.append("  INNER JOIN inscricao on inscricao.codigo       = resultadoprocessoseletivo.inscricao ");
		sb.append("  INNER JOIN pessoa on pessoa.codigo             = inscricao.candidato ");
		sb.append("  INNER JOIN procseletivo on procseletivo.codigo = inscricao.procseletivo ");
		sb.append(" inner join unidadeensinocurso on case when resultadoprimeiraopcao = 'AP' then  inscricao.cursoopcao1 else ");
		sb.append(" case when resultadosegundaopcao = 'AP' then inscricao.cursoopcao2 else inscricao.cursoopcao3 end end  = unidadeensinocurso.codigo ");
		sb.append("  inner join curso on curso.codigo = unidadeensinocurso.curso ");
		sb.append("  inner join turno on turno.codigo = unidadeensinocurso.turno ");
		sb.append(" WHERE inscricao.procseletivo = ").append(procSeletivoVO.getCodigo().intValue());
		sb.append(" AND (inscricao.cursoopcao1   = ").append(curso.intValue());
		sb.append(" OR inscricao.cursoopcao2     = ").append(curso.intValue());
		sb.append(" OR inscricao.cursoopcao3     = ").append(curso.intValue()).append(") ");
		sb.append(" AND (resultadoprimeiraopcao = 'AP' OR resultadosegundaopcao = 'AP' OR resultadoterceiraopcao = 'AP') ");
		sb.append(" ORDER BY classificacao,CASE WHEN (procseletivo.regimeaprovacao = 'quantidadeAcertosRedacao') THEN (medianotasprocseletivo + notaredacao) ELSE medianotasprocseletivo END DESC, ");
		sb.append(" notaredacao DESC, datanasc ");
		
		sb.append(" ) as t ");
		if(itemProcSeletivoDataProvaVO != null && itemProcSeletivoDataProvaVO > 0){
			sb.append(" WHERE  codigoDataProva = ").append(itemProcSeletivoDataProvaVO);
		}else{
			sb.append(" WHERE 1=1 "); 
		}
		sb.append(" AND t.numeroInscricao NOT IN (select distinct inscricao FROM matricula WHERE inscricao IS NOT NULL) ");
		sb.append(" AND (t.candidatoconvocadomatricula = false OR t.candidatoconvocadomatricula IS NULL)  ");
		sb.append(" ORDER BY classificacao");
		sb.append(" LIMIT ").append(qtdeCandidatoChamar);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<ChamadaCandidatoAprovadoRelVO> listaChamadaCandidatoAprovadoRelVOs = new ArrayList<ChamadaCandidatoAprovadoRelVO>(0);
		while (tabelaResultado.next()) {
			ChamadaCandidatoAprovadoRelVO obj = new ChamadaCandidatoAprovadoRelVO();
			montarDadosCandidatosAprovados(obj, tabelaResultado, quantidadeCasaDecimalConsiderarNotaProcessoSeletivo, usuarioVO);
			obj.setNumeroVaga(numeroVagas);
			obj.setNrCandidatosMatriculados(nrCandidatosMatriculados);
			obj.setNrVagasDisponiveis(numeroVagas - nrCandidatosMatriculados) ;
			obj.setQtdeCandidatosChamar(qtdeCandidatoChamar);
			listaChamadaCandidatoAprovadoRelVOs.add(obj);
		}
		return listaChamadaCandidatoAprovadoRelVOs;
	}

	public void montarDadosCandidatosAprovados(ChamadaCandidatoAprovadoRelVO obj, SqlRowSet dadosSQL, Integer quantidadeCasaDecimalConsiderarNotaProcessoSeletivo, UsuarioVO usuarioVO) {
		obj.setNomeCandidato(dadosSQL.getString("pessoa.nome"));
		obj.setClassificacao(dadosSQL.getInt("classificacao"));
		obj.setTelefoneRes(dadosSQL.getString("telefoneRes"));
		obj.setCelular(dadosSQL.getString("celular"));
		obj.setCpf(dadosSQL.getString("cpf"));
		obj.setEmail(dadosSQL.getString("email"));
		obj.setDataNascimento(Uteis.getDataAno4Digitos(dadosSQL.getDate("dataNasc")));				
		obj.setNumeroInscricao(dadosSQL.getInt("numeroInscricao"));
		if(dadosSQL.getString("regimeaprovacao").equals("quantidadeAcertosRedacao")){
			obj.setMediaNotasProcSeletivo(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(dadosSQL.getBigDecimal("medianotasprocseletivo").add(dadosSQL.getBigDecimal("notaredacao")), quantidadeCasaDecimalConsiderarNotaProcessoSeletivo));
		}else{
			obj.setMediaNotasProcSeletivo(dadosSQL.getBigDecimal("medianotasprocseletivo"));		
		}
	}

	public List montarListaSelectItemProcSeletivo(ProcSeletivoVO procSeletivoVO, UnidadeEnsinoVO unidadeEnsino, UsuarioVO usuarioVO) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		resultadoConsulta = consultarProcSeletivoPorDescricao(unidadeEnsino, usuarioVO);
		Ordenacao.ordenarListaDecrescente(resultadoConsulta, "dataInicio");
		i = resultadoConsulta.iterator();
		List objs = new ArrayList(0);
		objs.add(new SelectItem(0, ""));
		while (i.hasNext()) {
			ProcSeletivoVO obj = (ProcSeletivoVO) i.next();
			//if ((obj.getDataInicio().before(new Date()) && obj.getDataFim().after(new Date()) && obj.getDataFim().compareTo(new Date()) > 0) || (obj.getDataInicioInternet().before(new Date()) && obj.getDataFimInternet().after(new Date()) && obj.getDataFimInternet().compareTo(new Date()) > 0) || (obj.getCodigo().equals(procSeletivoVO.getCodigo()))) {
				objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
			//}
		}
		return objs;
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>descricao</code> Este atributo é
	 * uma lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarProcSeletivoPorDescricao(UnidadeEnsinoVO unidadeEnsino, UsuarioVO usuarioVO) throws Exception {
		List lista = getFacadeFactory().getProcSeletivoFacade().consultarPorDescricaoUnidadeEnsino("", unidadeEnsino.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);
		return lista;
	}

	
	public List montarListaSelectItemUnidadeEnsino(ProcSeletivoVO procSeletivoVO, UnidadeEnsinoVO unidadeEnsinoLogado, UsuarioVO usuarioVO) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		resultadoConsulta = consultarUnidadeEnsinoPorNome(unidadeEnsinoLogado, usuarioVO);
		procSeletivoVO.setProcSeletivoUnidadeEnsinoVOs(ProcSeletivoUnidadeEnsino.consultarProcSeletivoUnidadeEnsinos(procSeletivoVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO));
		i = resultadoConsulta.iterator();
		List objs = new ArrayList(0);
		while (i.hasNext()) {
			UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
			List unidadesValidas = procSeletivoVO.getProcSeletivoUnidadeEnsinoVOs();
			Iterator j = unidadesValidas.iterator();
			while (j.hasNext()) {
				ProcSeletivoUnidadeEnsinoVO unidadeProcessoSeletivo = (ProcSeletivoUnidadeEnsinoVO) j.next();
				if (unidadeProcessoSeletivo.getUnidadeEnsino().getCodigo().equals(obj.getCodigo())) {
					objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
					break;
				}
			}
		}
		return (objs);
	}

	public List consultarUnidadeEnsinoPorNome(UnidadeEnsinoVO unidadeEnsinoLogado, UsuarioVO usuarioVO) throws Exception {
		List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("", unidadeEnsinoLogado.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);
		return lista;
	}
	
	public Integer consultarQtdeCandidatosAprovadosNaoMatriculadoPorProcSeletivoCurso(Integer procSeletivo, Integer curso, Integer unidadeEnsino, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct count(inscricao.codigo) from resultadoprocessoseletivo   ");
		sb.append(" inner join inscricao on inscricao.codigo = resultadoprocessoseletivo.inscricao  ");
		sb.append(" where inscricao.procseletivo = ").append(procSeletivo.intValue());
		sb.append(" and (inscricao.cursoopcao1 = ").append(curso.intValue());
		sb.append(" or inscricao.cursoopcao2 = ").append(curso.intValue());
		sb.append(" or inscricao.cursoopcao3 = ").append(curso.intValue()).append(") ");
		sb.append(" and (resultadoprimeiraopcao = 'AP' or resultadosegundaopcao = 'AP' or resultadoterceiraopcao = 'AP') ");
		sb.append(" and inscricao.codigo not in(select distinct inscricao from matricula where inscricao is not null) ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("count");
		}
		return 0;
	}
	
	public Integer consultarQtdeCandidatosMatriculadosPorProcSeletivoCurso(Integer procSeletivo, Integer curso, Integer unidadeEnsino, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct count(matricula.matricula) from matricula ");
		sb.append(" inner join inscricao on inscricao.codigo = matricula.inscricao ");
		sb.append(" where inscricao.procseletivo = ").append(procSeletivo);
		sb.append(" and matricula.curso = ").append(curso);
		sb.append(" and matricula.unidadeensino = ").append(unidadeEnsino);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("count");
		}
		return 0;
	}
	
	public Integer consultarQtdeCandidatosConvocadoPorProcSeletivoCurso(Integer procSeletivo, Integer curso, Integer unidadeEnsino, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct count(inscricao.codigo) from inscricao ");
		sb.append(" where inscricao.procseletivo = ").append(procSeletivo.intValue());
		sb.append(" and (cursoopcao1 = ").append(curso.intValue());
		sb.append(" or cursoopcao2 = ").append(curso.intValue());
		sb.append(" or cursoopcao3 = ").append(curso.intValue()).append(") ");
		sb.append(" and unidadeensino = ").append(unidadeEnsino);
		sb.append(" and candidatoconvocadomatricula  = true ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("count");
		}
		return 0;
	}
	
	public void alterarSituacaoCandidatoParaConvocado(final List<ChamadaCandidatoAprovadoRelVO> listaChamadaCandidatoAprovadoRelVOs, int chamada, UsuarioVO usuarioVO) throws Exception {
		try {
			chamada++;
            final StringBuilder sql = new StringBuilder().append("UPDATE Inscricao set candidatoConvocadoMatricula = true, chamada = ").append(chamada).append(" WHERE codigo in(");
            boolean virgula = false;
            for (ChamadaCandidatoAprovadoRelVO chamadaCandidatoAprovadoRelVO : listaChamadaCandidatoAprovadoRelVOs) {
            	if (!virgula) {
            		sql.append("'").append(chamadaCandidatoAprovadoRelVO.getNumeroInscricao()).append("' ");
            		virgula = true;
            	}else {
            		sql.append(", '").append(chamadaCandidatoAprovadoRelVO.getNumeroInscricao()).append("' ");
            	}
			}
            sql.append(") ");
            
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
                    return sqlAlterar;
                }
            });
		} catch (Exception e) {
            throw e;
        }
	}
	
	public String getDesignIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator + "ChamadaCandidatoAprovadoRel.jrxml");
    }

    
    public String getCaminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator);
    }

    public static String getIdEntidade() {
        return ChamadaCandidatoAprovadoRel.idEntidade;
    }

    
    public void setIdEntidade(String idEntidade) {
    	ChamadaCandidatoAprovadoRel.idEntidade = idEntidade;
    }

}