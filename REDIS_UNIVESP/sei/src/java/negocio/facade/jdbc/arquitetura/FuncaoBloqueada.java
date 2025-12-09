package negocio.facade.jdbc.arquitetura;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.arquitetura.BloqueioFuncaoVO;
import negocio.comuns.arquitetura.FuncaoBloqueadaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.BloqueioFuncaoEnum;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.interfaces.arquitetura.FuncaoBloqueadaInterfaceFacade;

/**
 * 
 * @author
 */
@Repository
@Scope("singleton")
@Lazy
public class FuncaoBloqueada extends ControleAcesso implements FuncaoBloqueadaInterfaceFacade {

	private static final long serialVersionUID = 1L;

	public FuncaoBloqueada() throws Exception {
		super();
	}

	public BloqueioFuncaoVO montarListaTurmasBloqueada(List<Integer> listaTurma, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String listaDeCodigos = UtilReflexao.getObterCodigoParaDaListaParaConsulta(listaTurma);
		String sql = "SELECT codigo, identificadorTurma FROM Turma WHERE codigo in ( " + listaDeCodigos + " )";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql);
		BloqueioFuncaoVO obj = new BloqueioFuncaoVO();
		obj.setBloqueioFuncaoEnum(BloqueioFuncaoEnum.BLOQUEIO_TURMA_PROGRAMACAO_AULA);
		while (rs.next()) {
			FuncaoBloqueadaVO funcao = new FuncaoBloqueadaVO();
			funcao.setCodigo(rs.getInt("codigo"));
			funcao.setDescricao(rs.getString("identificadorTurma"));
			obj.getListaFuncaoBloqueadaVO().add(funcao);
		}
		return obj;
	}
	
	@Override
	public BloqueioFuncaoVO montarListaProfessorTurmas(List<Integer> listaProfessorTurma, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String listaDeCodigos = UtilReflexao.getObterCodigoParaDaListaParaConsulta(listaProfessorTurma);
		String sql = "SELECT codigo, nome FROM pessoa WHERE codigo in ( " + listaDeCodigos + " )";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql);
		BloqueioFuncaoVO obj = new BloqueioFuncaoVO();
		obj.setBloqueioFuncaoEnum(BloqueioFuncaoEnum.BLOQUEIO_PROFESSOR_TURMA_PROGRAMACAO_AULA);
		while (rs.next()) {
			FuncaoBloqueadaVO funcao = new FuncaoBloqueadaVO();
			funcao.setCodigo(rs.getInt("codigo"));
			funcao.setDescricao(rs.getString("nome"));
			obj.getListaFuncaoBloqueadaVO().add(funcao);
		}
		return obj;
	}

	public BloqueioFuncaoVO montarListaConfiguracaoNotaFiscalBloqueada(List<Integer> listaConfig, int nivelMontarDados, UsuarioVO usuario) {
		String listaDeCodigos = UtilReflexao.getObterCodigoParaDaListaParaConsulta(listaConfig);
		String sql = "SELECT codigo, nome FROM configuracaonotafiscal WHERE codigo in ( " + listaDeCodigos + " )";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql);
		BloqueioFuncaoVO obj = new BloqueioFuncaoVO();
		obj.setBloqueioFuncaoEnum(BloqueioFuncaoEnum.BLOQUEIO_NOTA_FISCAL);
		while (rs.next()) {
			FuncaoBloqueadaVO funcao = new FuncaoBloqueadaVO();
			funcao.setCodigo(rs.getInt("codigo"));
			funcao.setDescricao(rs.getString("nome"));
			obj.getListaFuncaoBloqueadaVO().add(funcao);
		}
		return obj;
	}
	
	public BloqueioFuncaoVO montarListaUnidadeEnsinoGeracaoParcela(List<Integer> listaUnidadeEnsino, int nivelMontarDados, UsuarioVO usuario) {
		String listaDeCodigos = UtilReflexao.getObterCodigoParaDaListaParaConsulta(listaUnidadeEnsino);
		String sql = "SELECT codigo, nome FROM unidadeensino WHERE codigo in ( " + listaDeCodigos + " )";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql);
		BloqueioFuncaoVO obj = new BloqueioFuncaoVO();
		obj.setBloqueioFuncaoEnum(BloqueioFuncaoEnum.BLOQUEIO_UNIDADE_ENSINO_GERACAO_PARCELA);
		while (rs.next()) {
			FuncaoBloqueadaVO funcao = new FuncaoBloqueadaVO();
			funcao.setCodigo(rs.getInt("codigo"));
			funcao.setDescricao(rs.getString("nome"));
			obj.getListaFuncaoBloqueadaVO().add(funcao);
		}
		return obj;
	}
	
	public BloqueioFuncaoVO montarProcessamentoProvaPresencial(UsuarioVO usuario) {
		BloqueioFuncaoVO obj = new BloqueioFuncaoVO();
		obj.setBloqueioFuncaoEnum(BloqueioFuncaoEnum.BLOQUEIO_PROCESSAMENTO_RROVA_PRESENCIAL);
		FuncaoBloqueadaVO funcao = new FuncaoBloqueadaVO();
		funcao.setCodigo(0);
		funcao.setDescricao("Processamento de Prova Presencial");
		obj.getListaFuncaoBloqueadaVO().add(funcao);
		return obj;
	}

	@Override
	public BloqueioFuncaoVO montarListaTurmaRealizandoRenovacao(int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String sql = "SELECT codigo FROM renovacaomatriculaturma WHERE situacao = 'EM_PROCESSAMENTO'";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql);
		BloqueioFuncaoVO obj = new BloqueioFuncaoVO();
		obj.setBloqueioFuncaoEnum(BloqueioFuncaoEnum.BLOQUEIO_TURMA_REALIZANDO_RENOVACAO);
		while (rs.next()) {
			FuncaoBloqueadaVO funcao = new FuncaoBloqueadaVO();
			funcao.setCodigo(rs.getInt("codigo"));
			obj.getListaFuncaoBloqueadaVO().add(funcao);
		}
		return obj;
	}
}
