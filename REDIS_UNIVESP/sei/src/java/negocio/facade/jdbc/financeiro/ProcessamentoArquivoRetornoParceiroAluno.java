package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ProcessamentoArquivoRetornoParceiroAlunoVO;
import negocio.comuns.financeiro.ProcessamentoArquivoRetornoParceiroExcelVO;
import negocio.comuns.financeiro.ProcessamentoArquivoRetornoParceiroVO;
import negocio.comuns.financeiro.enumerador.TipoListaProcessamentoArquivoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ProcessamentoArquivoRetornoParceiroAlunoInterfaceFacade;


@Repository
@Scope("singleton")
@Lazy
public class ProcessamentoArquivoRetornoParceiroAluno extends ControleAcesso implements ProcessamentoArquivoRetornoParceiroAlunoInterfaceFacade {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2969625297637981500L;
	protected static String idEntidade;
	
	

	public ProcessamentoArquivoRetornoParceiroAluno() throws Exception {
		super();
		setIdEntidade("ProcessamentoArquivoRetornoParceiroAluno");
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<ProcessamentoArquivoRetornoParceiroAlunoVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		for (ProcessamentoArquivoRetornoParceiroAlunoVO obj : lista) {
			persistir(obj, verificarAcesso, usuarioVO);	
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void persistir(ProcessamentoArquivoRetornoParceiroAlunoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (obj.getCodigo() == 0) {
			incluir(obj, verificarAcesso, usuarioVO);
		} else {
			alterar(obj, verificarAcesso, usuarioVO);
		}
		getFacadeFactory().getProcessamentoArquivoRetornoParceiroExcelFacade().persistir(obj.getListaContasAReceber(), false, usuarioVO);

	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ProcessamentoArquivoRetornoParceiroAlunoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			ProcessamentoArquivoRetornoParceiroAluno.incluir(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO ProcessamentoArquivoRetornoParceiroAluno (processamentoArquivoRetornoParceiro, cpf, valorRepasse, tipolistaprocessamentoarquivo ) ");
			sql.append("    VALUES ( ?, ?, ?, ? )");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int i = 0;
					sqlInserir.setInt(++i, obj.getProcessamentoArquivoRetornoParceiroVO().getCodigo());
					if(Uteis.isAtributoPreenchido(obj.getCpf())){
						sqlInserir.setString(++i, obj.getCpf());	
					}else{
						sqlInserir.setNull(++i, 0);
					}
					if(Uteis.isAtributoPreenchido(obj.getValorRepasse())){
						sqlInserir.setDouble(++i, obj.getValorRepasse());	
					}else{
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setString(++i, obj.getTipoListaProcessamentoArquivo().name());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>ProcessamentoArquivoRetornoParceiroVO</code>. Sempre utiliza a
	 * chave primária da classe como atributo para localização do registro a ser
	 * alterado. Primeiramente valida os dados (<code>validarDados</code>) do
	 * objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe
	 *            <code>ProcessamentoArquivoRetornoParceiroVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ProcessamentoArquivoRetornoParceiroAlunoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			ProcessamentoArquivoRetornoParceiroAluno.alterar(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE ProcessamentoArquivoRetornoParceiroAluno ");
			sql.append("   SET processamentoArquivoRetornoParceiro=?, cpf=?, valorRepasse=?, ");
			sql.append("   tipolistaprocessamentoarquivo=? ");
			sql.append(" WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int i = 0;
					
					sqlAlterar.setInt(++i, obj.getProcessamentoArquivoRetornoParceiroVO().getCodigo());
					if(Uteis.isAtributoPreenchido(obj.getCpf())){
						sqlAlterar.setString(++i, obj.getCpf());	
					}else{
						sqlAlterar.setNull(++i, 0);
					}
					if(Uteis.isAtributoPreenchido(obj.getValorRepasse())){
						sqlAlterar.setDouble(++i, obj.getValorRepasse());	
					}else{
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setString(++i, obj.getTipoListaProcessamentoArquivo().name());
					sqlAlterar.setInt(++i, obj.getCodigo());
					return sqlAlterar;
				}
			}) == 0) {
				incluir(obj, false, usuario);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void atualizarProcessamentoArquivoRetornoParceiroAlunoPorContaReceber(Integer matriculaPeriodo, SituacaoContaReceber situacaoContaReceber, UsuarioVO usuarioVO) throws Exception {
    	try {
    		StringBuilder sqlStr = new StringBuilder();
    		sqlStr.append("UPDATE processamentoarquivoretornoparceiroaluno set tipoListaProcessamentoArquivo = '").append(TipoListaProcessamentoArquivoEnum.NAO_LOCALIZADA_ARQUIVO.name()).append("' WHERE codigo in ( ");
    		sqlStr.append(" select distinct processamentoarquivoretornoparceiroaluno from processamentoarquivoretornoparceiroexcel  where contareceber in ( ");
    		sqlStr.append(" select codigo from contareceber where matriculaperiodo = ").append(matriculaPeriodo);
    		if(Uteis.isAtributoPreenchido(situacaoContaReceber)){
    			sqlStr.append(" and situacao = '").append(situacaoContaReceber.getValor()).append("' ");	
    		}
    		sqlStr.append(" )) ");
    		sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
    		getConexao().getJdbcTemplate().update(sqlStr.toString());
    	} catch (Exception e) {
    		throw e;
    	}
    }
	
	
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ProcessamentoArquivoRetornoParceiroAlunoVO> consultaRapidaPorProcessamentoArquivoRetornoParceiro(ProcessamentoArquivoRetornoParceiroVO obj, UsuarioVO usuario) throws Exception {
		StringBuffer sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE parpa.processamentoArquivoRetornoParceiro = ").append(obj.getCodigo());
		sql.append(" ORDER BY parpa.codigo ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<ProcessamentoArquivoRetornoParceiroAlunoVO> lista = new ArrayList<ProcessamentoArquivoRetornoParceiroAlunoVO>();
		while (tabelaResultado.next()) {
			ProcessamentoArquivoRetornoParceiroAlunoVO objAluno = consultarProcessamentoArquivoRetornoParceiroAlunoVOPorCodigo(tabelaResultado.getInt("parpa.codigo"), lista);
			montarDadosBasico(tabelaResultado, objAluno);
			addProcessamentoArquivoRetornoParceiroAlunoVOPorCodigo(objAluno, lista);
		}
		return lista;
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void montarDadosBasico(SqlRowSet dadosSQL, ProcessamentoArquivoRetornoParceiroAlunoVO obj) throws Exception {		
		if(!Uteis.isAtributoPreenchido(obj)){
			obj.setCodigo(dadosSQL.getInt("parpa.codigo"));
			obj.setCpf(dadosSQL.getString("parpa.cpf"));
			obj.getAluno().setCodigo(dadosSQL.getInt("pessoamatricula.codigo"));
			obj.getAluno().setNome(dadosSQL.getString("pessoamatricula.nome"));
			obj.setTipoListaProcessamentoArquivo(TipoListaProcessamentoArquivoEnum.valueOf(dadosSQL.getString("parpa.tipolistaprocessamentoarquivo")));
			obj.setValorRepasse(dadosSQL.getDouble("parpa.valorRepasse"));
			obj.getProcessamentoArquivoRetornoParceiroVO().setCodigo(dadosSQL.getInt("processamentoArquivoRetornoParceiro"));
		}
		ProcessamentoArquivoRetornoParceiroExcelVO objExcel = new ProcessamentoArquivoRetornoParceiroExcelVO();
		objExcel.setProcessamentoArquivoRetornoParceiroAlunoVO(obj);
		objExcel.setDataCompetencia(dadosSQL.getDate("parpe.datacompetencia"));
		objExcel.setValorConta(dadosSQL.getDouble("parpe.valorConta"));
		objExcel.getContaReceberVO().setCodigo(dadosSQL.getInt("contareceber"));
		objExcel.getContaReceberVO().setUpdated(dadosSQL.getDate("cr.updated"));
		objExcel.getContaReceberVO().setMatriculaPeriodo(dadosSQL.getInt("matriculaperiodo"));
		objExcel.getContaReceberVO().setTurma(getFacadeFactory().getTurmaFacade().consultarTurmaDoAlunoPorMatriculaPeriodo(objExcel.getContaReceberVO().getMatriculaPeriodo(), Uteis.NIVELMONTARDADOS_COMBOBOX, new UsuarioVO()));
		objExcel.getContaReceberVO().setNrDocumento(dadosSQL.getString("nrdocumento"));
		objExcel.getContaReceberVO().setNossoNumero(dadosSQL.getString("nossonumero"));
		objExcel.getContaReceberVO().setValor(dadosSQL.getDouble("cr.valor"));
		objExcel.getContaReceberVO().setValorRecebido(dadosSQL.getDouble("cr.valorrecebido"));
		objExcel.getContaReceberVO().setDataVencimento(dadosSQL.getDate("cr.datavencimento"));
		objExcel.getContaReceberVO().setSituacao(dadosSQL.getString("cr.situacao"));
		objExcel.getContaReceberVO().setTipoPessoa(dadosSQL.getString("cr.tipoPessoa"));
		objExcel.getContaReceberVO().getUnidadeEnsino().setCodigo(dadosSQL.getInt("m.unidadeensino"));
		objExcel.getContaReceberVO().getMatriculaAluno().setMatricula(dadosSQL.getString("cr.matriculaaluno"));
		objExcel.getContaReceberVO().getMatriculaAluno().getAluno().setCodigo(dadosSQL.getInt("pessoamatricula.codigo"));
		objExcel.getContaReceberVO().getMatriculaAluno().getAluno().setNome(dadosSQL.getString("pessoamatricula.nome"));
		objExcel.getContaReceberVO().getTurma().setCodigo(dadosSQL.getInt("turma.codigo"));
		objExcel.getContaReceberVO().getTurma().setIdentificadorTurma(dadosSQL.getString("turma.identificadorTurma"));

		if (objExcel.getContaReceberVO().getSituacao().equals(SituacaoContaReceber.RECEBIDO.getValor())) {
			// Monta os dados de Conta Receber para ser usado o relatorio de
			// Controle de Cobrança
			objExcel.getContaReceberVO().setValor(dadosSQL.getDouble("cr.valor"));
			objExcel.getContaReceberVO().setJuro(dadosSQL.getDouble("cr.juro"));
			objExcel.getContaReceberVO().setValorRecebido(dadosSQL.getDouble("cr.valorrecebido"));
			objExcel.getContaReceberVO().setMulta(dadosSQL.getDouble("cr.multa"));
			objExcel.getContaReceberVO().setValorDescontoRecebido(dadosSQL.getDouble("cr.valordescontorecebido"));
			objExcel.getContaReceberVO().setParcela(dadosSQL.getString("cr.parcela"));
			objExcel.getContaReceberVO().setDataVencimento(dadosSQL.getDate("cr.datavencimento"));
			objExcel.getContaReceberVO().setAcrescimo(dadosSQL.getDouble("cr.acrescimo"));
		}
		if (objExcel.getContaReceberVO().getTipoPessoa().equals("RF")) {
			objExcel.getContaReceberVO().getResponsavelFinanceiro().setCodigo(dadosSQL.getInt("resp.codigo"));
			objExcel.getContaReceberVO().getResponsavelFinanceiro().setNome(dadosSQL.getString("resp.nome"));
			objExcel.getContaReceberVO().getResponsavelFinanceiro().setCPF(dadosSQL.getString("resp.cpf"));
		} else if (objExcel.getContaReceberVO().getTipoPessoa().equals("PA")) {
			objExcel.getContaReceberVO().getParceiroVO().setCodigo(dadosSQL.getInt("parc.codigo"));
			objExcel.getContaReceberVO().getParceiroVO().setNome(dadosSQL.getString("parc.nome"));
			objExcel.getContaReceberVO().getParceiroVO().setCPF(dadosSQL.getString("parc.cpf"));
			objExcel.getContaReceberVO().getParceiroVO().setCNPJ(dadosSQL.getString("parc.cnpj"));
		} else if (objExcel.getContaReceberVO().getTipoPessoa().equals("FO")) {
			objExcel.getContaReceberVO().getFornecedor().setCodigo(dadosSQL.getInt("for.codigo"));
			objExcel.getContaReceberVO().getFornecedor().setNome(dadosSQL.getString("for.nome"));
			objExcel.getContaReceberVO().getFornecedor().setCPF(dadosSQL.getString("for.cpf"));
			objExcel.getContaReceberVO().getFornecedor().setCNPJ(dadosSQL.getString("for.cnpj"));
			objExcel.getContaReceberVO().getFornecedor().setTipoEmpresa(dadosSQL.getString("for.tipoEmpresa"));
		}		
		obj.getListaContasAReceber().add(objExcel);
	}
	
	

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private StringBuffer getSQLPadraoConsultaBasica() {
		StringBuffer sql = new StringBuffer(" SELECT parpa.codigo as \"parpa.codigo\",  ");
		sql.append(" parpa.processamentoArquivoRetornoParceiro,  ");
		sql.append(" parpa.valorRepasse as \"parpa.valorRepasse\", parpa.cpf as \"parpa.cpf\", ");
		sql.append(" parpa.tipolistaprocessamentoarquivo as \"parpa.tipolistaprocessamentoarquivo\", ");
		
		sql.append(" parpe.codigo as \"parpe.codigo\", parpe.contareceber, ");
		sql.append(" parpe.processamentoArquivoRetornoParceiroAluno, ");
		sql.append(" parpe.valorConta as \"parpe.valorConta\",  parpe.datacompetencia as \"parpe.datacompetencia\", ");

		sql.append(" contaReceber.nossonumero, contaReceber.nrdocumento, contaReceber.situacao as \"cr.situacao\", ");
		sql.append(" contareceber.updated as \"cr.updated\", contareceber.valor as \"cr.valor\",contareceber.juro as \"cr.juro\",contareceber.valorrecebido as \"cr.valorrecebido\",contareceber.multa as \"cr.multa\", ");
		sql.append(" contareceber.valordescontorecebido as \"cr.valordescontorecebido\",contareceber.parcela as \"cr.parcela\",contareceber.datavencimento as \"cr.datavencimento\", contareceber.acrescimo as \"cr.acrescimo\",  ");
		sql.append(" contareceber.tipoPessoa as \"cr.tipoPessoa\", contareceber.matriculaperiodo, contareceber.matriculaaluno as  \"cr.matriculaaluno\",  ");

		sql.append(" turma.codigo as \"turma.codigo\", turma.identificadorturma as \"turma.identificadorturma\",  ");
		sql.append(" m.unidadeensino as \"m.unidadeensino\",  ");
		sql.append(" pessoamatricula.codigo as \"pessoamatricula.codigo\", pessoamatricula.nome as \"pessoamatricula.nome\", pessoamatricula.cpf as \"pessoamatricula.cpf\",  ");

		sql.append(" parc.codigo as \"parc.codigo\", parc.nome as \"parc.nome\", parc.cpf as \"parc.cpf\", parc.cnpj as \"parc.cnpj\",  ");
		sql.append(" fornecedor.codigo as \"for.codigo\", fornecedor.nome as \"for.nome\" , fornecedor.cnpj as \"for.cnpj\" , fornecedor.cpf as \"for.cpf\",  fornecedor.tipoEmpresa as \"for.tipoEmpresa\", ");
		sql.append(" resp.codigo as \"resp.codigo\", resp.nome as \"resp.nome\", resp.cpf as \"resp.cpf\" ");
		sql.append(" FROM processamentoArquivoRetornoParceiroAluno parpa ");
		sql.append(" LEFT JOIN processamentoArquivoRetornoParceiroExcel parpe on  parpe.processamentoArquivoRetornoParceiroAluno = parpa.codigo");
		sql.append(" LEFT JOIN contaReceber on contaReceber.codigo = parpe.contaReceber ");
		sql.append(" LEFT JOIN matricula m ON contaReceber.matriculaaluno = m.matricula ");
		sql.append(" LEFT JOIN pessoa pessoamatricula ON m.aluno = pessoamatricula.codigo ");
		sql.append(" LEFT JOIN pessoa resp ON contaReceber.responsavelFinanceiro = resp.codigo ");
		sql.append(" LEFT JOIN convenio ON (convenio.codigo = contareceber.convenio) ");
		sql.append(" LEFT JOIN parceiro parc ON (parc.codigo = convenio.parceiro) ");
		sql.append(" LEFT JOIN fornecedor ON fornecedor.codigo = contaReceber.fornecedor ");
		sql.append(" LEFT JOIN turma ON turma.codigo = contareceber.turma  ");

		return sql;
	}
	
	
	@Transactional(readOnly = false, rollbackFor = {Throwable.class}, propagation = Propagation.SUPPORTS)
	private StringBuilder getSQLPadraoConsultaProcessamentoArquivoRetornoParceiro() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT contareceber.codigo, contareceber.registrocobrancacontareceber, contareceber.impressaoBoletoRealizada, contareceber.tipopessoa, contareceber.nrdocumento, contareceber.nossonumero, contareceber.nossonumerobanco, contareceber.pessoa, contareceber.notafiscalsaidaservico, pessoamatricula.nome AS \"Aluno.nome\", pessoa.nome AS \"Pessoa.nome\", pessoamatricula.cpf AS \"Pessoa.cpf\", pessoamatricula.email AS \"Pessoa.email\", contareceber.parcela, contareceber.contacorrente, cc.carteiraregistrada AS \"contacorrente.carteiraregistrada\", contareceber.codorigem, ");
		sb.append("contareceber.codigobarra, contareceber.linhadigitavelcodigobarras, contareceber.descontoProgressivo, contareceber.turma, contareceber.duplicidadeTratada, contareceber.descontoconvenio, contareceber.descontoinstituicao, contareceber.valordescontoprogressivo,  ");
		sb.append("contareceber.valor, contareceber.valorBaseContaReceber, contareceber.valorRecebido, contareceber.juro, contareceber.multa, contareceber.acrescimo, contareceber.dataVencimento, contareceber.dataCompetencia, contareceber.dataProcessamentoValorReceber, contareceber.situacao, contareceber.multaporcentagem, contareceber.juroporcentagem, contareceber.tipoorigem, ");
		sb.append("contareceber.valorDesconto, contareceber.tipoDesconto, contareceber.valordescontolancadorecebimento, contareceber.valorcalculadodescontolancadorecebimento, contareceber.tipodescontolancadorecebimento, contareceber.valorDescontoAlunoJaCalculado, contareceber.updated, contareceber.usaDescontoCompostoPlanoDesconto, ");
		sb.append(" contareceber.ordemConvenio, contareceber.OrdemConvenioValorCheio, contareceber.OrdemDescontoAluno, contareceber.matriculaperiodo, contaReceber.valorDescontoCalculado, contaReceber.valorReceberCalculado, contareceber.valorDescontoRateio,  ");
		sb.append(" contareceber.OrdemDescontoAlunoValorCheio, contareceber.OrdemDescontoProgressivo, contareceber.OrdemDescontoProgressivoValorCheio, ");
		sb.append(" contareceber.possuiPendenciasFinanceirasExternas, contareceber.OrdemPlanoDesconto, contareceber.OrdemPlanoDescontoValorCheio, contareceber.responsavelFinanceiro, contareceber.contareceberagrupada, ");
		sb.append(" contareceber.valorIndiceReajustePorAtraso, contareceber.possuiFiesIntegracaoFinanceiras, contareceber.valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa, ");
		sb.append(" indicereajuste.codigo as \"indicereajuste.codigo\" , indicereajuste.descricao as \"indicereajuste.descricao\", ");
		sb.append(" matricula.matricula AS \"Matricula.matricula\", funcionario.codigo AS \"Funcionario.codigo\", funcionario.matricula AS \"Funcionario.matricula\", pessoafuncionario.nome AS \"Funcionario.nome\", pessoafuncionario.codigo AS \"Funcionario.codigo\", unidadeEnsino.codigo AS \"UnidadeEnsino.codigo\", unidadeEnsino.nome As \"UnidadeEnsino.nome\", ");
		sb.append(" unidadeensinofinanceira.codigo AS \"unidadeensinofinanceira.codigo\", unidadeensinofinanceira.nome As \"unidadeensinofinanceira.nome\", ");
		sb.append(" pessoacandidato.nome AS \"Candidato.nome\", pessoacandidato.codigo AS \"Candidato.codigo\", pessoacandidato.cpf AS \"Candidato.cpf\", parceiro.nome AS \"Parceiro.nome\", parceiro.cnpj AS \"Parceiro.cnpj\", parceiro.cpf AS \"Parceiro.cpf\", parceiro.codigo AS \"Parceiro.codigo\", Parceiro.isentarJuro as \"Parceiro.isentarJuro\", Parceiro.isentarMulta as \"Parceiro.isentarMulta\", ");
		sb.append(" responsavelFinanceiro.nome as \"responsavelFinanceiro.nome\", responsavelFinanceiro.cpf as \"responsavelFinanceiro.cpf\", responsavelFinanceiro.email as \"responsavelFinanceiro.email\", ");
		sb.append(" turma.identificadorturma,fornecedor.codigo as \"fornecedor.codigo\", fornecedor.nome as \"fornecedor.nome\", fornecedor.rg as \"fornecedor.rg\", ");
		sb.append(" fornecedor.codigo AS \"fornecedor.codigo\", fornecedor.nome As \"fornecedor.nome\", fornecedor.rg AS \"fornecedor.rg\", fornecedor.cpf As \"fornecedor.cpf\", fornecedor.cnpj AS \"fornecedor.cnpj\", fornecedor.tipoEmpresa AS \"fornecedor.tipoEmpresa\", ");
		sb.append(" pessoamatricula.codigo as \"pessoamatricula.codigo\", pessoamatricula.nome as \"pessoamatricula.nome\",  pessoamatricula.cpf as \"pessoamatricula.cpf\", ");
		sb.append(" contareceber.pagocomdcc, contareceber.liberadoDoIndiceReajustePorAtraso, contareceber.contaEditadaManualmente, contareceber.utilizarDescontoProgressivoManual, ");
		sb.append(" case when contareceber.tipoorigem in ('MAT', 'MEN') then matricula.bloquearEmissaoBoletoMatMenVisaoAluno else false end as \"Matricula.bloquearEmissaoBoletoMatMenVisaoAluno\", ");	
		sb.append(" CentroReceita.codigo as \"CentroReceita.codigo\", CentroReceita.descricao as \"CentroReceita.descricao\", contareceber.processamentointegracaofinanceiradetalhe ");
		sb.append(" FROM contareceber ");
		sb.append(" inner JOIN convenio ON (convenio.codigo = contareceber.convenio) ");
		sb.append(" inner JOIN parceiro on parceiro.codigo = convenio.parceiro ");
		sb.append(" LEFT JOIN funcionario ON (funcionario.codigo = contareceber.funcionario) ");
		sb.append(" LEFT JOIN pessoa AS pessoafuncionario ON (pessoafuncionario.codigo = funcionario.pessoa) ");
		sb.append(" LEFT JOIN matricula ON (matricula.matricula = contareceber.matriculaaluno) ");
		sb.append(" LEFT JOIN pessoa AS pessoamatricula ON (matricula.aluno = pessoamatricula.codigo) ");
		sb.append(" LEFT JOIN pessoa AS pessoacandidato ON (contareceber.candidato = pessoacandidato.codigo) ");
		sb.append(" LEFT JOIN pessoa ON (contareceber.pessoa = pessoa.codigo) ");
		sb.append(" LEFT JOIN pessoa as responsavelFinanceiro ON (contareceber.responsavelFinanceiro = responsavelFinanceiro.codigo) ");
		sb.append(" LEFT JOIN unidadeensino ON (unidadeensino.codigo = contareceber.unidadeensino) ");
		sb.append(" LEFT JOIN unidadeensino unidadeensinofinanceira ON (unidadeensinofinanceira.codigo = contareceber.unidadeensinofinanceira)");
		sb.append(" LEFT JOIN centroreceita ON (centroreceita.codigo = contareceber.centroreceita) ");		
		sb.append(" LEFT JOIN turma ON (turma.codigo = contareceber.turma) ");
		sb.append(" LEFT JOIN fornecedor ON (fornecedor.codigo = contareceber.fornecedor) ");
		sb.append(" LEFT JOIN contacorrente cc ON (contareceber.contacorrente = cc.codigo) ");
		sb.append(" LEFT JOIN indicereajuste ON (contareceber.indiceReajustePadraoPorAtraso = indicereajuste.codigo) ");
		return sb;
	}
	
	
	
	@Override
	@Transactional(readOnly = false, rollbackFor = {Throwable.class}, propagation = Propagation.SUPPORTS)
	public List<ProcessamentoArquivoRetornoParceiroAlunoVO> consultarContaReceberBolsaCusteadaConvenioPorParceiroPorMesCompetencia(ProcessamentoArquivoRetornoParceiroVO processamento, UsuarioVO usuarioVO) throws Exception {
		List<ProcessamentoArquivoRetornoParceiroAlunoVO> lista = new ArrayList<>();
		StringBuilder sb = getSQLPadraoConsultaProcessamentoArquivoRetornoParceiro();
		sb.append(" where contareceber.tipoorigem  = 'BCC' and contareceber.situacao = 'AR'");
		if(!processamento.getListaCodigoContaReceberEncontradosNoArquivo().isEmpty()){
			sb.append(" and contareceber.codigo not in (").append(UteisTexto.converteListaInteiroParaString(processamento.getListaCodigoContaReceberEncontradosNoArquivo())).append(") ");	
		}
		sb.append(" and parceiro.codigo = ").append(processamento.getParceiroVO().getCodigo());
		sb.append(" and unidadeensino.codigo = ").append(processamento.getUnidadeEnsinoVO().getCodigo());
		String datas = processamento.getDatasDeCompetencia().replaceAll(";", "','");
		sb.append(" and to_char(contareceber.datacompetencia, 'MM/yyyy') in ('").append(datas.substring(0, datas.length()-2)).append(") ");
		sb.append(" order by pessoamatricula.codigo ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			ProcessamentoArquivoRetornoParceiroAlunoVO obj = consultarProcessamentoArquivoRetornoParceiroAlunoVO(tabelaResultado.getString("pessoamatricula.nome"), lista);
			if(!Uteis.isAtributoPreenchido(obj.getAluno())){
				obj.getAluno().setCodigo(tabelaResultado.getInt("pessoamatricula.codigo"));
				obj.getAluno().setNome(tabelaResultado.getString("pessoamatricula.nome"));
				obj.setMatricula(tabelaResultado.getString("Matricula.matricula"));
			}
			ProcessamentoArquivoRetornoParceiroExcelVO objExcel = new ProcessamentoArquivoRetornoParceiroExcelVO();
			getFacadeFactory().getContaReceberFacade().montarDadosBasico(objExcel.getContaReceberVO(), tabelaResultado);
			objExcel.setDataCompetencia(tabelaResultado.getDate("dataCompetencia"));
			objExcel.setProcessamentoArquivoRetornoParceiroAlunoVO(obj);
			obj.getListaContasAReceber().add(objExcel);
			addProcessamentoArquivoRetornoParceiroAlunoVO(obj, lista);
		}
		return lista;
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private ProcessamentoArquivoRetornoParceiroAlunoVO consultarProcessamentoArquivoRetornoParceiroAlunoVOPorCodigo(Integer codigo, List<ProcessamentoArquivoRetornoParceiroAlunoVO> lista) {
		for (ProcessamentoArquivoRetornoParceiroAlunoVO objsExistente : lista) {
			if (objsExistente.getCodigo().equals(codigo)) {
				return objsExistente;
			}
		}
		return new ProcessamentoArquivoRetornoParceiroAlunoVO();
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void addProcessamentoArquivoRetornoParceiroAlunoVOPorCodigo(ProcessamentoArquivoRetornoParceiroAlunoVO obj, List<ProcessamentoArquivoRetornoParceiroAlunoVO> lista) {
		int index = 0;
		for (ProcessamentoArquivoRetornoParceiroAlunoVO objsExistente : lista) {
			if (objsExistente.getCodigo().equals(obj.getCodigo())) {
				lista.set(index, obj);
				return;
			}
			index++;
		}
		lista.add(obj);
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private ProcessamentoArquivoRetornoParceiroAlunoVO consultarProcessamentoArquivoRetornoParceiroAlunoVO(String nomeAluno, List<ProcessamentoArquivoRetornoParceiroAlunoVO> lista) {
		for (ProcessamentoArquivoRetornoParceiroAlunoVO objsExistente : lista) {
			if (objsExistente.getAluno().getNome().equals(nomeAluno)) {
				return objsExistente;
			}
		}
		return new ProcessamentoArquivoRetornoParceiroAlunoVO();
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void addProcessamentoArquivoRetornoParceiroAlunoVO(ProcessamentoArquivoRetornoParceiroAlunoVO obj, List<ProcessamentoArquivoRetornoParceiroAlunoVO> lista) {
		int index = 0;
		for (ProcessamentoArquivoRetornoParceiroAlunoVO objsExistente : lista) {
			if (objsExistente.getAluno().getNome().equals(obj.getAluno().getNome())) {
				lista.set(index, obj);
				return;
			}
			index++;
		}
		lista.add(obj);
	}
	
	
	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return ProcessamentoArquivoRetornoParceiroAluno.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ProcessamentoArquivoRetornoParceiroAluno.idEntidade = idEntidade;
	}

	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void atualizarProcessamentoArquivoRetornoParceiroAlunoPorContaReceberEspecifica(Integer contaReceber, UsuarioVO usuarioVO) throws Exception {
    	try {
    		StringBuilder sqlStr = new StringBuilder();
    		sqlStr.append("UPDATE processamentoarquivoretornoparceiroaluno set tipoListaProcessamentoArquivo = '").append(TipoListaProcessamentoArquivoEnum.NAO_LOCALIZADA_ARQUIVO.name()).append("' WHERE codigo in ( ");
    		sqlStr.append(" select distinct processamentoarquivoretornoparceiroaluno from processamentoarquivoretornoparceiroexcel  where contareceber = ? ) ");
    		sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
    		getConexao().getJdbcTemplate().update(sqlStr.toString(), contaReceber);
    	} catch (Exception e) {
    		throw e;
    	}
    }
}
