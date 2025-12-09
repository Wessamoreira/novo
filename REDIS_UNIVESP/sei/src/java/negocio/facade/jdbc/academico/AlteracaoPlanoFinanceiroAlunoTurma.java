package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.AlteracaoPlanoFinanceiroAlunoTurmaVO;
import negocio.comuns.academico.CondicaoPagamentoPlanoFinanceiroCursoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PlanoFinanceiroCursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.enumeradores.ResultadoEnum;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ProcessarParalelismo;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.AlteracaoPlanoFinanceiroAlunoTurmaFacade;

/**
 * Classe de persistencia que encapsula todas as operacaoes de
 * manipulacao dos dados da classe <code>AlteracaoPlanoFinanceiroAlunoTurmaVO</code>. responsavel por
 * implementar operacoes como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>AlteracaoPlanoFinanceiroAlunoTurmaVO</code>. 
 * Encapsula toda a interacao com o banco de dados.
 * 
 * @see AlteracaoPlanoFinanceiroAlunoTurmaVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class AlteracaoPlanoFinanceiroAlunoTurma extends ControleAcesso implements AlteracaoPlanoFinanceiroAlunoTurmaFacade {

	private static final long serialVersionUID = 5417763200004301817L;
	
	protected static String idEntidade;

	public AlteracaoPlanoFinanceiroAlunoTurma() throws Exception {
		super();
		setIdEntidade("AlteracaoPlanoFinanceiroAlunoTurma");
	}

	@Override
	public void setIdEntidade(String idEntidade) {
		AlteracaoPlanoFinanceiroAlunoTurma.idEntidade = idEntidade;
	}
	
	public static MatriculaVO montarDadosMatricula(String matricula, int nivelMontarDados, UsuarioVO usuario)
			throws Exception {
		return getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(matricula, null, NivelMontarDados.BASICO, usuario); 
	}
	
	public static CondicaoPagamentoPlanoFinanceiroCursoVO montarDadosCondicaoPagamento(Integer codigoCondicaoPagamento, int nivelMontarDados, UsuarioVO usuario)
			throws Exception {
		return getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().consultarPorChavePrimaria(codigoCondicaoPagamento, nivelMontarDados, usuario);
	}
	
	public static MatriculaPeriodoVO montarDadosMatriculaPeriodo(Integer codigoMatriculaPeriodo, int nivelMontarDados, UsuarioVO usuario)
			throws Exception {
		return getFacadeFactory().getMatriculaPeriodoFacade().consultarPorChavePrimaria(codigoMatriculaPeriodo, nivelMontarDados, null, usuario);
	}
	
	public static UsuarioVO montarDadosUsuario(Integer codigoUsuario, int nivelMontarDados, UsuarioVO usuario)
			throws Exception {
		return getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(codigoUsuario, nivelMontarDados, usuario);
	}

	public static AlteracaoPlanoFinanceiroAlunoTurmaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {

		AlteracaoPlanoFinanceiroAlunoTurmaVO obj = new AlteracaoPlanoFinanceiroAlunoTurmaVO();
		
		obj.setMatriculaVO(montarDadosMatricula(dadosSQL.getString("matricula"), nivelMontarDados, usuario));
		obj.setLabelCondicaoPagamentoPlanoFinanceiroCursoVOAntigo(dadosSQL.getString("labelCondicaoPagamentoAntigo"));
		
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			return obj;
		}
		
		obj.setOpcoes(dadosSQL.getString("opcoes"));
		obj.setMatriculaPeriodoVO(montarDadosMatriculaPeriodo(dadosSQL.getInt("matriculaperiodo"), nivelMontarDados, usuario));
		
		if(nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS)
			return obj;
		
		obj.getCondicaoPagamentoPlanoFinanceiroCursoVONovo().setCodigo(dadosSQL.getInt("condicaopagamentoatual"));
		obj.getCondicaoPagamentoPlanoFinanceiroCursoVONovo().setDescricao(dadosSQL.getString("descricaCondicaoAtual"));
		obj.getCondicaoPagamentoPlanoFinanceiroCursoVONovo().setCategoria(dadosSQL.getString("categoriaCondicaoAtual"));
		obj.getCondicaoPagamentoPlanoFinanceiroCursoVONovo().setNrParcelasPeriodo(dadosSQL.getInt("nrParcelaPeriodoAtual"));
		obj.getCondicaoPagamentoPlanoFinanceiroCursoVONovo().setValorParcela(dadosSQL.getDouble("valorParcelaAtual"));
		
		obj.getCondicaoPagamentoPlanoFinanceiroCursoVOAntigo().setCodigo(dadosSQL.getInt("condicaopagamentoanterior"));
		obj.getCondicaoPagamentoPlanoFinanceiroCursoVOAntigo().setDescricao(dadosSQL.getString("descricaoCondicaoAnterior"));
		obj.getCondicaoPagamentoPlanoFinanceiroCursoVOAntigo().setCategoria(dadosSQL.getString("categoriaCondicaoAnterior"));
		obj.getCondicaoPagamentoPlanoFinanceiroCursoVOAntigo().setNrParcelasPeriodo(dadosSQL.getInt("nrParcelaPeriodoAnterior"));
		obj.getCondicaoPagamentoPlanoFinanceiroCursoVOAntigo().setValorParcela(dadosSQL.getDouble("valorParcelaAnterior"));
		
		obj.setUsuarioVO(montarDadosUsuario(dadosSQL.getInt("usuarioalteracao"), nivelMontarDados, usuario));
		obj.setSituacaoAlteracao(dadosSQL.getString("situacaoalteracao"));
		obj.setDataAlteracao(dadosSQL.getTimestamp("dataalteracao"));
		obj.setLogErro(dadosSQL.getString("logerro"));
		
		return obj;
	}
	
	@Override
	public List<AlteracaoPlanoFinanceiroAlunoTurmaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<AlteracaoPlanoFinanceiroAlunoTurmaVO> listaAlteracoesPlanoFinanceiro = new ArrayList<AlteracaoPlanoFinanceiroAlunoTurmaVO>(0);
		while (tabelaResultado.next()) {
			listaAlteracoesPlanoFinanceiro.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return listaAlteracoesPlanoFinanceiro;
	}
	
	@Override
	public List<AlteracaoPlanoFinanceiroAlunoTurmaVO> consultarAlunosDaTurmaComParcelasGeradas(Integer turma, int nivelMontarDados, UsuarioVO usuarioVO)
			throws Exception {
		
		StringBuilder sql = new StringBuilder();
        sql.append(" select distinct on (m.matricula) m.matricula, p.nome as aluno, mp.condicaoPagamentoPlanoFinanceiroCurso, ");
        sql.append(" pl.descricao || ' - ' || coalesce((cp.categoria ),'') || ' - ' || cp.descricao ")
        .append(" || ' - ' || cp.nrParcelasPeriodo || ' x ' || to_char(cp.valorParcela, 'L9G999G990D99') as labelCondicaoPagamentoAntigo, '' as opcoes from turma t ");
        sql.append(" inner join matriculaperiodo mp on t.codigo = mp.turma ");
        sql.append(" inner join condicaopagamentoplanofinanceirocurso cp on cp.codigo = mp.condicaopagamentoplanofinanceirocurso "); 
        sql.append(" inner join planofinanceirocurso pl on pl.codigo = mp.planofinanceirocurso  ");
        sql.append(" inner join matricula m on m.matricula = mp.matricula  ");
        sql.append(" inner join pessoa p on p.codigo = m.aluno ");
        sql.append(" inner join contareceber cr on cr.pessoa = p.codigo and cr.matriculaaluno = m.matricula and (cr.tipoorigem like 'MEN' or cr.tipoorigem like 'MDI') ");
        sql.append(" where t.codigo = ").append(turma);
        sql.append(" order by m.matricula desc ");
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioVO);
		
	}

	@Override
	public List<AlteracaoPlanoFinanceiroAlunoTurmaVO> consultarAlunosComCondicaoPagamentoNaoEncontrado(Integer turma, Integer codigoNovoPlanoFinanceiroCurso, String categoria, 
			int nivelmontardadosDadosbasicos, UsuarioVO usuarioLogado) throws Exception {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select matricula.matricula, condicaopagamentoplanofinanceirocurso.valorparcela, condicaopagamentoplanofinanceirocurso.nrparcelasperiodo, matriculaperiodo.codigo, ")
		.append(" pl.descricao || ' - ' || coalesce((condicaopagamentoplanofinanceirocurso.categoria ),'') || ' - ' || condicaopagamentoplanofinanceirocurso.descricao ")
        .append(" || ' - ' || condicaopagamentoplanofinanceirocurso.nrParcelasPeriodo || ' x ' || to_char(condicaopagamentoplanofinanceirocurso.valorParcela, 'L9G999G990D99') as labelCondicaoPagamentoAntigo, ")
		.append("  '' as opcoes from matricula ")
		.append(" inner join matriculaperiodo  on matriculaperiodo.matricula = matricula.matricula ")
		.append(" and matriculaperiodo.codigo = ( select mp.codigo from matriculaperiodo as mp where mp.matricula = matricula.matricula ")
		.append(" order by (mp.ano || '/' || mp.semestre) desc, case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc limit 1 ) ")
		.append(" inner join condicaopagamentoplanofinanceirocurso  on matriculaperiodo.condicaopagamentoplanofinanceirocurso = condicaopagamentoplanofinanceirocurso.codigo ")
		.append(" inner join planofinanceirocurso pl on pl.codigo = matriculaperiodo.planofinanceirocurso ")
		.append(" inner join turma on matriculaperiodo.turma = turma.codigo ")
		
		.append(" where turma.codigo = ").append(turma)
		
		.append(" and not exists ( select planofinanceirocurso.codigo from planofinanceirocurso ")
		.append(" inner join condicaopagamentoplanofinanceirocurso cppfc  on planofinanceirocurso.codigo = cppfc.planofinanceirocurso ")
		.append(" where planofinanceirocurso.codigo = ").append(codigoNovoPlanoFinanceiroCurso)
		.append(" and cppfc.nrparcelasperiodo = condicaopagamentoplanofinanceirocurso.nrparcelasperiodo ")
		.append(" and cppfc.situacao =  'AT'");
		if(!categoria.equals("")) {
			sql.append(" and cppfc.categoria =  '").append(categoria).append("'");			
		}
		sql.append(") ")
		.append(" and not exists ( select cr.codigo from contareceber cr where cr.matriculaaluno = matricula.matricula and (cr.tipoorigem like 'MDI' or cr.tipoorigem like 'MEN')) ")
        .append(" order by matricula.matricula desc ");
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        
		return montarDadosConsulta(tabelaResultado, nivelmontardadosDadosbasicos, usuarioLogado);
	}

	@Override
	public List<AlteracaoPlanoFinanceiroAlunoTurmaVO> consultarAlunosQueSeraoAlterados(Integer turma, Integer codigoNovoPlanoFinanceiroCurso, String categoria,
			int nivelmontardadosDadosbasicos, UsuarioVO usuarioLogado) throws Exception {
		
		StringBuilder sql = new StringBuilder();
        sql.append(" select matricula.matricula, condicaopagamentoplanofinanceirocurso.valorparcela, condicaopagamentoplanofinanceirocurso.nrparcelasperiodo, condicaopagamentoplanofinanceirocurso.categoria, ")
        .append(" pl.descricao || ' - ' || coalesce((condicaopagamentoplanofinanceirocurso.categoria ),'') || ' - ' || condicaopagamentoplanofinanceirocurso.descricao ")
        .append(" || ' - ' || condicaopagamentoplanofinanceirocurso.nrParcelasPeriodo || ' x ' || to_char(coalesce(condicaopagamentoplanofinanceirocurso.valorParcela,'0'), 'L9G999G990D99') as labelCondicaoPagamentoAntigo, ")
        .append(" array_to_string(array( ")
        .append("     select '<condicaopagamentoplanofinanceirocurso><codigo>'||cppfc.codigo||'</codigo><descricao>'||cppfc.descricao||'</descricao>'||")
        .append("'<categoria>'||cppfc.categoria||'</categoria>'||")
        .append("'<nrParcelasPeriodo>'||cppfc.nrParcelasPeriodo||'</nrParcelasPeriodo><valorParcela>'||cppfc.valorParcela||'</valorParcela></condicaopagamentoplanofinanceirocurso>' ")
        .append(" from planofinanceirocurso ") 
        .append(" inner join condicaopagamentoplanofinanceirocurso cppfc  on planofinanceirocurso.codigo = cppfc.planofinanceirocurso ")
        .append(" where planofinanceirocurso.codigo = ").append(codigoNovoPlanoFinanceiroCurso)
        .append(" and cppfc.nrparcelasperiodo = condicaopagamentoplanofinanceirocurso.nrparcelasperiodo ")
        .append(" and cppfc.valorparcela < condicaopagamentoplanofinanceirocurso.valorparcela ")
        .append(" and cppfc.situacao =  'AT' ");
        if(!categoria.equals("")) {
			sql.append(" and cppfc.categoria =  '").append(categoria).append("'");			
		}
        sql.append(" ), '&;&') as opcoes, matriculaperiodo.codigo, matriculaperiodo.codigo as matriculaperiodo")
        .append(" from matricula ")
        .append(" inner join matriculaperiodo  on matriculaperiodo.matricula = matricula.matricula ")
        .append(" and matriculaperiodo.codigo = ( ")
        .append(" select mp.codigo from matriculaperiodo as mp ")
        .append(" where mp.matricula = matricula.matricula ")
        .append(" order by (mp.ano || '/' || mp.semestre) desc, case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc limit 1 ) ")
        .append(" inner join condicaopagamentoplanofinanceirocurso  on matriculaperiodo.condicaopagamentoplanofinanceirocurso = condicaopagamentoplanofinanceirocurso.codigo ")
        .append(" inner join planofinanceirocurso pl on pl.codigo = matriculaperiodo.planofinanceirocurso ")
		.append(" inner join turma on matriculaperiodo.turma = turma.codigo ")
        .append(" where turma.codigo = ").append(turma)
        .append(" and exists ( ")
        .append(" select planofinanceirocurso.codigo from planofinanceirocurso ")
        .append(" inner join condicaopagamentoplanofinanceirocurso cppfc  on planofinanceirocurso.codigo = cppfc.planofinanceirocurso ")
        .append(" where planofinanceirocurso.codigo = ").append(codigoNovoPlanoFinanceiroCurso)
        .append(" and cppfc.nrparcelasperiodo = condicaopagamentoplanofinanceirocurso.nrparcelasperiodo ")
        .append(" and cppfc.valorparcela < condicaopagamentoplanofinanceirocurso.valorparcela ")
        .append(" and cppfc.situacao =  'AT' ");
        if(!categoria.equals("")) {
			sql.append(" and cppfc.categoria =  '").append(categoria).append("'");			
		}
        sql.append(" ) ")
        .append(" and not exists ( select cr.codigo from contareceber cr where cr.matriculaaluno = matricula.matricula and (cr.tipoorigem like 'MDI' or cr.tipoorigem like 'MEN')) ");
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        
		return montarDadosConsulta(tabelaResultado, nivelmontardadosDadosbasicos, usuarioLogado);
		
	}

	@Override
	public List<AlteracaoPlanoFinanceiroAlunoTurmaVO> consultarAlunosComValorInferiorAoNovoPlano(Integer turma, Integer codigoNovoPlanoFinanceiroCurso, String categoria,
			int nivelmontardadosDadosbasicos, UsuarioVO usuarioLogado) throws Exception {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("select matricula.matricula, condicaopagamentoplanofinanceirocurso.valorparcela, condicaopagamentoplanofinanceirocurso.nrparcelasperiodo, matriculaperiodo.codigo, ")
		.append(" pl.descricao || ' - ' || coalesce((condicaopagamentoplanofinanceirocurso.categoria ),'') || ' - ' || condicaopagamentoplanofinanceirocurso.descricao ")
        .append(" || ' - ' || condicaopagamentoplanofinanceirocurso.nrParcelasPeriodo || ' x ' || to_char(coalesce(condicaopagamentoplanofinanceirocurso.valorParcela,'0'), 'L9G999G990D99') as labelCondicaoPagamentoAntigo, ")
		.append(" '' as opcoes ")
		.append(" from matricula ")
		.append(" inner join matriculaperiodo  on matriculaperiodo.matricula = matricula.matricula ")
		.append(" and matriculaperiodo.codigo = ( select mp.codigo from matriculaperiodo as mp where mp.matricula = matricula.matricula ")
		.append(" order by (mp.ano || '/' || mp.semestre) desc, case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc limit 1) ")
		.append(" inner join condicaopagamentoplanofinanceirocurso on matriculaperiodo.condicaopagamentoplanofinanceirocurso = condicaopagamentoplanofinanceirocurso.codigo ")
		.append(" inner join planofinanceirocurso pl on pl.codigo = matriculaperiodo.planofinanceirocurso ")
		.append(" inner join turma on matriculaperiodo.turma = turma.codigo ");
		
		sql.append(" where turma.codigo = ").append(turma)
		.append(" and exists ( select planofinanceirocurso.codigo from planofinanceirocurso ")
		.append(" inner join condicaopagamentoplanofinanceirocurso cppfc  on planofinanceirocurso.codigo = cppfc.planofinanceirocurso ")
		.append(" where planofinanceirocurso.codigo = ").append(codigoNovoPlanoFinanceiroCurso)
		.append(" and cppfc.nrparcelasperiodo = condicaopagamentoplanofinanceirocurso.nrparcelasperiodo ")
		.append(" and cppfc.situacao =  'AT' ");
		
		if(!categoria.equals("")) {
			sql.append(" and cppfc.categoria = '").append(categoria).append("'");
		}
		
		sql.append(") and not exists ( select planofinanceirocurso.codigo  from planofinanceirocurso ")
		.append(" inner join condicaopagamentoplanofinanceirocurso cppfc  on planofinanceirocurso.codigo = cppfc.planofinanceirocurso ")
		.append(" where planofinanceirocurso.codigo = ").append(codigoNovoPlanoFinanceiroCurso)
		.append(" and cppfc.nrparcelasperiodo = condicaopagamentoplanofinanceirocurso.nrparcelasperiodo ")
		.append(" and cppfc.valorparcela <= condicaopagamentoplanofinanceirocurso.valorparcela ")
		.append(" and cppfc.situacao =  'AT' ");
		
		if(!categoria.equals("")) {
			sql.append(" and cppfc.categoria = '").append(categoria).append("'");
		}
		
		sql.append(" ) and not exists ( select cr.codigo from contareceber cr where cr.matriculaaluno = matricula.matricula and (cr.tipoorigem like 'MDI' or cr.tipoorigem like 'MEN')) ")
        .append(" order by matricula.matricula desc ");
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        
		return montarDadosConsulta(tabelaResultado, nivelmontardadosDadosbasicos, usuarioLogado);
		
	}
	
	@Override
	public List<AlteracaoPlanoFinanceiroAlunoTurmaVO> consultarAlunosComCondicaoPagamentoEmConformidade(Integer turma, Integer codigoNovoPlanoFinanceiroCurso, String categoria, 
			int nivelmontardadosDadosbasicos, UsuarioVO usuarioLogado) throws Exception {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select matricula.matricula, condicaopagamentoplanofinanceirocurso.valorparcela, condicaopagamentoplanofinanceirocurso.nrparcelasperiodo, matriculaperiodo.codigo,  ")
		.append(" pl.descricao || ' - ' || coalesce((condicaopagamentoplanofinanceirocurso.categoria ),'') || ' - ' || condicaopagamentoplanofinanceirocurso.descricao ")
        .append(" || ' - ' || condicaopagamentoplanofinanceirocurso.nrParcelasPeriodo || ' x ' || to_char(condicaopagamentoplanofinanceirocurso.valorParcela, 'L9G999G990D99') as labelCondicaoPagamentoAntigo ")
		.append(" from matricula  ")
		.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ") 
		.append(" and matriculaperiodo.codigo = ( select mp.codigo from matriculaperiodo as mp where mp.matricula = matricula.matricula ") 
		.append(" order by (mp.ano || '/' || mp.semestre) desc, case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc limit 1 ) ") 
		.append(" inner join condicaopagamentoplanofinanceirocurso  on matriculaperiodo.condicaopagamentoplanofinanceirocurso = condicaopagamentoplanofinanceirocurso.codigo ") 
		.append(" inner join planofinanceirocurso pl on pl.codigo = matriculaperiodo.planofinanceirocurso   ")
		.append(" inner join turma on matriculaperiodo.turma = turma.codigo and turma.planoFinanceiroCurso = matriculaperiodo.planoFinanceiroCurso ");
		
		if(!categoria.trim().equals("")) {
			sql.append(" and matriculaperiodo.categoriaCondicaoPagamento like '").append(categoria).append("'");
		}
		
		sql.append(" where turma.codigo = ").append(turma);
		sql.append(" and matriculaperiodo.planoFinanceiroCurso = ").append(codigoNovoPlanoFinanceiroCurso);
		
		sql.append(" and not exists ( select cr.codigo from contareceber cr where cr.matriculaaluno = matricula.matricula and (cr.tipoorigem like 'MDI' or cr.tipoorigem like 'MEN')) ")
        .append(" order by matricula.matricula desc ");
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        
		return montarDadosConsulta(tabelaResultado, nivelmontardadosDadosbasicos, usuarioLogado);
	}
	
	@Override
	public void alterarPlanoFinanceiroAlunoConformeDadosDaTurma(final List<AlteracaoPlanoFinanceiroAlunoTurmaVO> listaDeAlunosQueSeraoAlterados, final ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, final UsuarioVO usuarioVO, final TurmaVO turmaVO, final ProgressBarVO progressBarVO) throws Exception {
		
		final ConsistirException  consistirException = new ConsistirException();
		ProcessarParalelismo.executar(0, listaDeAlunosQueSeraoAlterados.size(), consistirException, new ProcessarParalelismo.Processo() {
			
			@Override
			public void run(int i) {
				AlteracaoPlanoFinanceiroAlunoTurmaVO alteracaoAluno = listaDeAlunosQueSeraoAlterados.get(i);
				try {
					realizarAtualiacaoProgressBar(progressBarVO);
					alteracaoAluno = popularTodosOsDados(turmaVO, alteracaoAluno, configuracaoFinanceiroVO, usuarioVO);		
					progressBarVO.setStatus("Processando Matrícula "+alteracaoAluno.getMatriculaVO().getMatricula());
					PlanoFinanceiroCursoVO planoFinanceiroVO = getFacadeFactory().getPlanoFinanceiroCursoFacade().consultarPorChavePrimaria(alteracaoAluno.getCondicaoPagamentoPlanoFinanceiroCursoVONovo().getPlanoFinanceiroCurso(), "", Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);					
					//Salvar por aluno
					getFacadeFactory().getMatriculaFacade().alterarPlanoFinanceiroAlunoConformeDadosTurma(alteracaoAluno.getMatriculaVO(), alteracaoAluno.getMatriculaPeriodoVO(), 
							planoFinanceiroVO, alteracaoAluno.getCondicaoPagamentoPlanoFinanceiroCursoVONovo().getCategoria(), 
							alteracaoAluno.getCondicaoPagamentoPlanoFinanceiroCursoVONovo(), configuracaoFinanceiroVO, usuarioVO);
					
					alteracaoAluno.setSituacaoAlteracao(ResultadoEnum.SUCESSO.name());
					alteracaoAluno.setLogErro("");
					incluir(alteracaoAluno);
				} catch (Exception e) {
					alteracaoAluno.setSituacaoAlteracao(ResultadoEnum.FALHA.name());
					//Salva o log de erro com ate 255 caracteres que e o tamanho maximo do varchar
					alteracaoAluno.setLogErro(e.getMessage().length() > 254 ? e.getMessage().substring(0, 254) : e.getMessage());
					incluir(alteracaoAluno);
				}
				
			}
		});
				
	}
	
	private synchronized void realizarAtualiacaoProgressBar(ProgressBarVO progressBarVO) {
		progressBarVO.incrementar();
		
	}

	private AlteracaoPlanoFinanceiroAlunoTurmaVO popularTodosOsDados(TurmaVO turmaVO,
			AlteracaoPlanoFinanceiroAlunoTurmaVO alteracaoAluno, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		
		alteracaoAluno.setUsuarioVO(usuarioVO);
		alteracaoAluno.setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(turmaVO.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
		getFacadeFactory().getMatriculaPeriodoFacade().carregarDados(alteracaoAluno.getMatriculaPeriodoVO(), NivelMontarDados.FORCAR_RECARGATODOSOSDADOS, configuracaoFinanceiroVO, usuarioVO);
		alteracaoAluno.setCondicaoPagamentoPlanoFinanceiroCursoVOAntigo(getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().consultarPorChavePrimaria(alteracaoAluno.getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		alteracaoAluno.setCondicaoPagamentoPlanoFinanceiroCursoVONovo(getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().consultarPorChavePrimaria(alteracaoAluno.getCondicaoPagamentoPlanoFinanceiroCursoVONovo().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
		
		alteracaoAluno.getMatriculaPeriodoVO().setCategoriaCondicaoPagamento(turmaVO.getCategoriaCondicaoPagamento());
		
		return alteracaoAluno;
	}

	private void incluir(AlteracaoPlanoFinanceiroAlunoTurmaVO alteracaoAluno) {
		
		StringBuilder sql = new StringBuilder("INSERT INTO alteracaoplanofinanceiroalunoturma ( turma, matricula, matriculaperiodo, condicaopagamentoatual, condicaopagamentoanterior, situacaoalteracao, usuarioalteracao, dataalteracao, logErro) ")
		.append("VALUES (?,?,?,?,?,?,?,?,?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(alteracaoAluno.getUsuarioVO()));
		
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				
				PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
				sqlInserir.setInt(1, alteracaoAluno.getTurmaVO().getCodigo());
				sqlInserir.setString(2, alteracaoAluno.getMatriculaVO().getMatricula());
				sqlInserir.setInt(3, alteracaoAluno.getMatriculaPeriodoVO().getCodigo());
				sqlInserir.setInt(4, alteracaoAluno.getCondicaoPagamentoPlanoFinanceiroCursoVONovo().getCodigo());
				sqlInserir.setInt(5, alteracaoAluno.getCondicaoPagamentoPlanoFinanceiroCursoVOAntigo().getCodigo());
				sqlInserir.setString(6, alteracaoAluno.getSituacaoAlteracao());
				sqlInserir.setInt(7, alteracaoAluno.getUsuarioVO().getCodigo());
				sqlInserir.setTimestamp(8, Uteis.getDataJDBCTimestamp(new Date()));
				sqlInserir.setString(9, alteracaoAluno.getLogErro());
				return sqlInserir;
			}
		});
	}

	@Override
	public List<AlteracaoPlanoFinanceiroAlunoTurmaVO> consultarAlunosQueSofreramAlteracaoNaCondicaoPagamento(TurmaVO turmaVO, int nivelmontardadosDadosbasicos, UsuarioVO usuarioLogado) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select matricula, '' as labelCondicaoPagamentoAntigo, '' as opcoes, matriculaperiodo, condicaopagamentoatual, condicaopagamentoanterior, ")
		.append(" condAtual.descricao as descricaCondicaoAtual, condAtual.categoria as categoriaCondicaoAtual, condAtual.nrparcelasperiodo as nrParcelaPeriodoAtual, condAtual.valorparcela valorParcelaAtual, ")
		.append(" condAnterior.descricao descricaoCondicaoAnterior, condAnterior.categoria as categoriaCondicaoAnterior, condAnterior.nrparcelasperiodo nrParcelaPeriodoAnterior, condAnterior.valorparcela valorParcelaAnterior, ")
		.append(" situacaoalteracao, dataalteracao, usuarioalteracao, logerro from alteracaoplanofinanceiroalunoturma  ")
		.append(" inner join condicaopagamentoplanofinanceirocurso condAnterior on condAnterior.codigo = alteracaoplanofinanceiroalunoturma.condicaopagamentoanterior ")
		.append(" inner join condicaopagamentoplanofinanceirocurso condAtual on condAtual.codigo = alteracaoplanofinanceiroalunoturma.condicaopagamentoatual ")
		.append(" where turma =  ").append(turmaVO.getCodigo())
		
		.append(" order by dataalteracao, matricula desc ");
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        
        try {
        	return montarDadosConsulta(tabelaResultado, nivelmontardadosDadosbasicos, usuarioLogado);        	
        } catch (Exception e) {
        	e.printStackTrace();
        	return new ArrayList<>();
		}
	}
}