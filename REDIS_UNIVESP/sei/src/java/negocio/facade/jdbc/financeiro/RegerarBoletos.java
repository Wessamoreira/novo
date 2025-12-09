package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.AgenciaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ControleRemessaContaReceberVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.Bancos;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.RegerarBoletosInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>AgenciaVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>AgenciaVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see AgenciaVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class RegerarBoletos extends ControleAcesso implements RegerarBoletosInterfaceFacade {

    protected static String idEntidade;
    private String numeroDocumento;
    private int qtdeContas = 0;

    public RegerarBoletos() throws Exception {
        super();
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void regerarBoletos(List<ContaReceberVO> listaContaReceberVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO, ControleRemessaContaReceberVO crcr) throws Exception {
        try {
            int i = 0;
           // //System.out.println("Inicio: " + new Date());
            for (ContaReceberVO contaReceberVO : listaContaReceberVO) {
                gerarDadosBoleto(contaReceberVO, configuracaoFinanceiroVO, usuarioVO, crcr);
                i++;
                if (i % 100 == 0) {
                   // //System.out.println(new Date());
                }
            }
            ////System.out.println("Fim: " + new Date());
            setQtdeContas(i);
        } catch (Exception e) {
            throw e;
        }
    }

    //este metodo foi depreciado . ao utilizar este metodo falar com o pedro ou edson , pois o metodo foi depreciado pelo fato de causar erro na geraçao do nosso numero pela tela de atualizaçao de vencimentos , foi alterado para o metodo gerarDados Boleto em contaReceber
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void gerarDadosBoleto(ContaReceberVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO, ControleRemessaContaReceberVO crcr) throws Exception {
	/*	String linhaDigitavel = "";		
		if (obj.getLinhaDigitavelCodigoBarras() == null || obj.getLinhaDigitavelCodigoBarras().equals("")) {
			throw new Exception("O campo linha digitável da conta a receber encontra-se vazio. Matrícula aluno: "+obj.getMatriculaAluno().getMatricula()+", nosso número: "+obj.getNossoNumero()+" e data de vencimento "+obj.getDataVencimento_Apresentar()+"   ");
		} else {
			linhaDigitavel = obj.getLinhaDigitavelCodigoBarras();
		}
		try {
			//verificar se o banco é SICOOB ou CAIXA e se o boleto tem remessa.
			if ((crcr.getCodigo().intValue() > 0) && (obj.getContaCorrenteVO().getAgencia().getBanco().getNrBanco().equals(Bancos.SICOOB.getNumeroBanco()) 
					|| obj.getContaCorrenteVO().getAgencia().getBanco().getNrBanco().equals(Bancos.CAIXA_ECONOMICA_FEDERAL.getNumeroBanco())
					|| obj.getContaCorrenteVO().getAgencia().getBanco().getNrBanco().equals(Bancos.BANCO_DO_BRASIL.getNumeroBanco())
					|| obj.getContaCorrenteVO().getAgencia().getBanco().getNrBanco().equals(Bancos.BRADESCO.getNumeroBanco())
					|| obj.getContaCorrenteVO().getAgencia().getBanco().getNrBanco().equals(Bancos.SANTANDER.getNumeroBanco())
					|| obj.getContaCorrenteVO().getAgencia().getBanco().getNrBanco().equals(Bancos.ITAU.getNumeroBanco())
					)) {
				obj.setConfiguracaoFinanceiro(configuracaoFinanceiroVO);
				obj.setNossoNumero("");
				String sqlStr = "UPDATE contareceber SET sequencialnossonumero = null WHERE codigo = ?"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
				getConexao().getJdbcTemplate().update(sqlStr, new Object[] { obj.getCodigo() });
				getFacadeFactory().getContaReceberFacade().gerarNumeroDoc(obj, obj.getContaCorrenteVO().getAgencia().getBanco().getNrBanco(), usuarioVO);
				if (configuracaoFinanceiroVO.getGerarBoletoComDescontoSemValidade()) {
					obj.setGerarBoletoComDescontoSemValidade(Boolean.TRUE);
					getFacadeFactory().getContaReceberFacade().executarCalculoValorBoletoDescontoSemValidade(obj, configuracaoFinanceiroVO, usuarioVO);
				}
				obj.criarBoleto(obj.getContaCorrenteVO());
			} else {
				gravarNumeroDoc(obj);
				/// verificar se o boleto possui remessa, caso sim nao modifica nossonumero, caso exista, deve ser gerado novo nossonumero para os bancos CAIXA e SICOOB.
				ContaCorrenteVO contaCorrenteVO = consultarContaCorrentePorChavePrimaria(obj.getContaCorrente());
				if (!linhaDigitavel.equals(obj.getLinhaDigitavelCodigoBarras())) {
					executarGeracaoNossoNumeroContaReceber(obj);
				}
				if (configuracaoFinanceiroVO.getGerarBoletoComDescontoSemValidade()) {
	                obj.setGerarBoletoComDescontoSemValidade(Boolean.TRUE);
	                getFacadeFactory().getContaReceberFacade().executarCalculoValorBoletoDescontoSemValidade(obj, configuracaoFinanceiroVO, usuarioVO);
	            } 
	            obj.criarBoleto(contaCorrenteVO);
			}
			alterar(obj);
		} finally {
			linhaDigitavel = null;
		}*/
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ContaReceberVO obj) throws Exception {
        final String sql = "UPDATE ContaReceber set codigobarra=?, linhadigitavelcodigobarras=?, nossonumero=? WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
                sqlAlterar.setString(1, obj.getCodigoBarra());
                sqlAlterar.setString(2, obj.getLinhaDigitavelCodigoBarras());
                sqlAlterar.setString(3, obj.getNossoNumero());
                sqlAlterar.setInt(4, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });
    }

    public void executarGeracaoNossoNumeroContaReceber(final ContaReceberVO obj) throws Exception {
        String baseNossoNumero = obj.getCodigo().toString();
        String tipoNumeracaoGeracao = "0";
        String numeroParcela = "";
        if ((obj.getTipoOrigem().equals("MAT")) || (obj.getTipoOrigem().equals("MEN"))) {
            if (!obj.getCodOrigem().equals("")) {
                // assumi-se o codigo de origem como base para geracao do nosso numero,
                // caso ele seja diferente de vazio.
                baseNossoNumero = obj.getCodOrigem();
            }
            if (obj.getCodOrigem().length() > 7) {
                throw new Exception("Erro ao gerar o nosso número para controle de boletagem da conta a receber de "
                        + "código: " + obj.getCodigo() + ". O campo base de geração ultrapassou o limite máximo "
                        + "permitido pelos Bancos (tamanho máximo 7). ");
            }
            tipoNumeracaoGeracao = "1";
            numeroParcela = obj.obterNumeroParcela();
            baseNossoNumero = tipoNumeracaoGeracao + numeroParcela
                    + Uteis.preencherComZerosPosicoesVagas(baseNossoNumero, 7);
            obj.setNossoNumero(baseNossoNumero);
        } else {
            obj.setNossoNumero(Uteis.preencherComZerosPosicoesVagas(baseNossoNumero, 10));
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void gravarNumeroDoc(ContaReceberVO obj) throws Exception {
        UnidadeEnsinoVO unidadeEnsinoVO = null;
        if(obj.getUnidadeEnsino().getCodigo() != null && obj.getUnidadeEnsino().getCodigo() > 0){
            unidadeEnsinoVO = consultarUnidadeEnsinoPorCodigo(obj.getUnidadeEnsino().getCodigo());
        }else{
            unidadeEnsinoVO = new UnidadeEnsinoVO();
        }
        obj.setNrDocumento(gerarNumeroDocumento(unidadeEnsinoVO));
//        if (obj.getNossoNumero().equals("")) {
//            executarGeracaoNossoNumeroContaReceber(obj);
//        }
//        alterarNrDocumentoNossoNumero(obj);
        alterarNrDocumento(obj);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarNrDocumentoNossoNumero(final ContaReceberVO obj) throws Exception {
        final String sql = "UPDATE ContaReceber set nrDocumento=?, nossonumero=? WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
                sqlAlterar.setString(1, obj.getNrDocumento());
                sqlAlterar.setString(2, obj.getNossoNumero());
                sqlAlterar.setInt(3, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarNrDocumento(final ContaReceberVO obj) throws Exception {
        final String sql = "UPDATE ContaReceber set nrDocumento=? WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
                sqlAlterar.setString(1, obj.getNossoNumero());
                sqlAlterar.setInt(2, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });
    }

    public String gerarNumeroDocumento(UnidadeEnsinoVO obj) throws Exception {
        numeroDocumento = Uteis.getCompletarNumeroComZero(2, obj.getCodigo().intValue());
        if (Uteis.getAnoDataAtual4Digitos().equals(obj.getAno())) {
            numeroDocumento += obj.getAno();
        } else {
            Integer novoAno = Integer.parseInt(obj.getAno()) + 1;
            obj.setAno(String.valueOf(novoAno));
            obj.setNumeroDocumento(1);
            numeroDocumento += obj.getAno();
        }
        numeroDocumento += Uteis.getCompletarNumeroComZero(7, obj.getNumeroDocumento().intValue() + 1);
        obj.setNumeroDocumento(obj.getNumeroDocumento() + 1);
        if (consultarPorContaReceberPorNrDocumento(numeroDocumento)) {
            gerarNumeroDocumento(obj);
        } else {
            getFacadeFactory().getUnidadeEnsinoFacade().alterarAnoNumeroDocumento(obj);
        }
        return numeroDocumento;
    }

    public Boolean consultarPorContaReceberPorNrDocumento(String nrDocumento) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT COUNT(*) >= 1 AS existe FROM contareceber WHERE nrdocumento = ? ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[]{nrDocumento});
        if (!tabelaResultado.next()) {
            return false;
        }
        return tabelaResultado.getBoolean("existe");
    }

    public UnidadeEnsinoVO consultarUnidadeEnsinoPorCodigo(Integer codigoPrm) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT unidadeensino.codigo, unidadeensino.ano, unidadeensino.numerodocumento ");
        sql.append("from unidadeensino ");
        sql.append("where unidadeensino.codigo = ?");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( unidadeensino ).");
        }
        return (montarDadosUnidadeEnsino(tabelaResultado));
    }

    private UnidadeEnsinoVO montarDadosUnidadeEnsino(SqlRowSet dadosSQL) {
        UnidadeEnsinoVO obj = new UnidadeEnsinoVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setAno(dadosSQL.getString("ano"));
        obj.setNumeroDocumento(dadosSQL.getInt("numerodocumento"));

        return obj;
    }

    public ContaCorrenteVO consultarContaCorrentePorChavePrimaria(Integer codigoPrm) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT contacorrente.codigo, contacorrente.carteira, contacorrente.convenio, contacorrente.numero AS \"ContaCorrente.numero\", contacorrente.agencia, contacorrente.digito AS \"contaCorrente.digito\", ");
        sql.append("agencia.numeroagencia, banco.codigo AS \"Banco.codigo\", banco.nrbanco, contacorrente.codigocedente, banco.modelogeracaoboleto ");
        sql.append("from contacorrente ");
        sql.append("LEFT JOIN agencia ON agencia.codigo = contacorrente.agencia ");
        sql.append("LEFT JOIN banco ON banco.codigo = agencia.banco ");
        sql.append("where contacorrente.codigo = ?");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( ContaCorrente ).");
        }
        return (montarDadosContaCorrente(tabelaResultado));
    }

    private ContaCorrenteVO montarDadosContaCorrente(SqlRowSet dadosSQL) {
        ContaCorrenteVO obj = new ContaCorrenteVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setCarteira(dadosSQL.getString("carteira"));
        obj.setConvenio(dadosSQL.getString("convenio"));
        obj.setNumero(dadosSQL.getString("ContaCorrente.numero"));
        obj.setDigito(dadosSQL.getString("contaCorrente.digito"));
        obj.getAgencia().setCodigo(dadosSQL.getInt("agencia"));
        obj.getAgencia().setNumeroAgencia(dadosSQL.getString("numeroagencia"));
        obj.getAgencia().getBanco().setCodigo(dadosSQL.getInt("Banco.codigo"));
        obj.getAgencia().getBanco().setNrBanco(dadosSQL.getString("nrbanco"));
        obj.setCodigoCedente(dadosSQL.getString("codigocedente"));
        obj.getAgencia().getBanco().setModeloGeracaoBoleto(dadosSQL.getString("modelogeracaoboleto"));
        return obj;
    }

    public List consultarContaReceber(int offset, int codUnidadeEnsino, int codUnidadeEnsinoCurso, int codTurma, String ano, String semestre) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT contareceber.codigo, contareceber.tipoorigem, contareceber.codorigem, contareceber.unidadeensino, contareceber.contacorrente, contareceber.data, contareceber.dataVencimento, ");
        sql.append("contareceber.valor, contareceber.situacao, contareceber.nrdocumento, contareceber.nossonumero, contareceber.parcela, contareceber.tipopessoa, contareceber.pessoa, ");
        sql.append("contareceber.parceiro, contareceber.codigobarra, contareceber.linhadigitavelcodigobarras, ");
        sql.append("matricula.matricula AS \"Matricula.matricula\", pessoamatricula.nome AS \"Pessoa.nome\", pessoamatricula.cpf AS \"Pessoa.cpf\",");
        sql.append("pessoacandidato.codigo AS \"Candidato.codigo\", pessoacandidato.nome AS \"Candidato.nome\", pessoacandidato.cpf AS \"Candidato.cpf\", ");
        sql.append("funcionario.matricula AS \"Funcionario.matricula\", pessoafuncionario.nome AS \"Funcionario.nome\", pessoafuncionario.codigo AS \"Funcionario.codigo\", ");
        sql.append("parceiro.codigo AS \"Parceiro.codigo\", parceiro.nome AS \"Parceiro.nome\", parceiro.cnpj AS \"Parceiro.cnpj\", parceiro.cpf AS \"Parceiro.cpf\" ");
        sql.append("FROM contareceber ");
        if (codUnidadeEnsino != 0) {
            sql.append("LEFT JOIN unidadeEnsino ON (unidadeensino.codigo = contareceber.unidadeensino) ");
        }
        if (codUnidadeEnsinoCurso != 0) {
            sql.append("LEFT JOIN unidadeEnsinoCurso ON (unidadeensinocurso.unidadeensino = contareceber.unidadeensino) ");
        }
        if (codTurma != 0 || !ano.equals("") || !semestre.equals("")) {
            sql.append("LEFT JOIN matriculaperiodoturmadisciplina ON (matriculaperiodoturmadisciplina.matricula = contareceber.matriculaaluno) ");
        }
        if (!ano.equals("") || !semestre.equals("")) {
            sql.append("LEFT JOIN matriculaperiodo ON (matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo) ");
        }
        sql.append("LEFT JOIN funcionario ON (funcionario.codigo = contareceber.funcionario) ");
        sql.append("LEFT JOIN parceiro ON (parceiro.codigo = contareceber.parceiro) ");
        sql.append("LEFT JOIN pessoa AS pessoafuncionario ON (pessoafuncionario.codigo = funcionario.pessoa) ");
        sql.append("LEFT JOIN matricula ON (matricula.matricula = contareceber.matriculaaluno) ");
        sql.append("LEFT JOIN pessoa AS pessoamatricula ON (matricula.aluno = pessoamatricula.codigo) ");
        sql.append("LEFT JOIN pessoa AS pessoacandidato ON (contareceber.candidato = pessoacandidato.codigo) ");
        sql.append("LEFT JOIN pessoa ON (contareceber.pessoa = pessoa.codigo) ");
        sql.append("WHERE contareceber.situacao = 'AR' AND (contareceber.tituloimportadosistemalegado = false OR contareceber.tituloimportadosistemalegado isNull) ");
        if (codUnidadeEnsino != 0) {
                 sql.append("AND contareceber.unidadeensino = ").append(codUnidadeEnsino).append(" ");
        }
        if (codUnidadeEnsinoCurso != 0) {
                sql.append("AND unidadeensinocurso.codigo = ").append(codUnidadeEnsinoCurso).append(" ");
        }
        if (codTurma != 0) {
                sql.append("AND matriculaperiodoturmadisciplina.turma = ").append(codTurma).append(" ");
        }
        if (!ano.equals("")) {
            sql.append("AND matriculaperiodo.ano = '").append(ano).append("' ");
        }
        if(!semestre.equals("")){
            sql.append("AND matriculaperiodo.semestre = '").append(semestre).append("' ");
        }
        sql.append("ORDER BY contareceber.codigo ");
        sql.append("LIMIT 10000 OFFSET " + offset);

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return (montarDadosConsulta(tabelaResultado));
    }

    public List montarDadosConsulta(SqlRowSet tabelaResultado) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            ContaReceberVO contaReceber = new ContaReceberVO();
            vetResultado.add(montarDados(contaReceber, tabelaResultado));
        }
        return vetResultado;
    }

    private ContaReceberVO montarDados(ContaReceberVO obj, SqlRowSet dadosSQL) {
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setTipoOrigem(dadosSQL.getString("tipoorigem"));
        obj.setCodOrigem(dadosSQL.getString("codorigem"));
        obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeensino"));
        obj.setContaCorrente(dadosSQL.getInt("contacorrente"));
        obj.setData(dadosSQL.getDate("data"));
        obj.setDataVencimento(dadosSQL.getDate("datavencimento"));
        obj.setValor(dadosSQL.getDouble("valor"));
        obj.setSituacao(dadosSQL.getString("situacao"));
        obj.setNrDocumento(dadosSQL.getString("nrdocumento"));
        obj.setNossoNumero(dadosSQL.getString("nossonumero"));
        obj.setParcela(dadosSQL.getString("parcela"));
        obj.setTipoPessoa(dadosSQL.getString("tipopessoa"));
        obj.setCodigoBarra(dadosSQL.getString("codigobarra"));
        obj.setLinhaDigitavelCodigoBarras(dadosSQL.getString("linhadigitavelcodigobarras"));
        obj.getPessoa().setCodigo(dadosSQL.getInt("pessoa"));
        obj.getPessoa().setNome(dadosSQL.getString("Pessoa.nome"));
        obj.getPessoa().setCPF(dadosSQL.getString("Pessoa.cpf"));
        switch (TipoPessoa.getEnum(obj.getTipoPessoa())) {
            case ALUNO:
                obj.getMatriculaAluno().setMatricula(dadosSQL.getString("Matricula.matricula"));
                obj.getMatriculaAluno().getAluno().setNome(obj.getPessoa().getNome());
                obj.getMatriculaAluno().getAluno().setCodigo(obj.getPessoa().getCodigo());
                break;
            case CANDIDATO:
                obj.getCandidato().setNome(dadosSQL.getString("Candidato.nome"));
                obj.getCandidato().setCodigo(dadosSQL.getInt("Candidato.codigo"));
                obj.getCandidato().setCPF(dadosSQL.getString("Candidato.cpf"));
                break;
            case FUNCIONARIO:
                obj.getFuncionario().setMatricula(dadosSQL.getString("Funcionario.matricula"));
                obj.getFuncionario().getPessoa().setNome(dadosSQL.getString("Funcionario.nome"));
                obj.getFuncionario().getPessoa().setCodigo(dadosSQL.getInt("Funcionario.codigo"));
                break;
            case PARCEIRO:
                obj.getParceiroVO().setNome(dadosSQL.getString("Parceiro.nome"));
                obj.getParceiroVO().setCPF(dadosSQL.getString("Parceiro.cpf"));
                obj.getParceiroVO().setCNPJ(dadosSQL.getString("Parceiro.cnpj"));
                obj.getParceiroVO().setCodigo(dadosSQL.getInt("Parceiro.codigo"));
                break;
        }
        return obj;
    }

    public String getNumeroDocumento() {
        if (numeroDocumento == null) {
            numeroDocumento = "";
        }
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public int getQtdeContas() {
        return qtdeContas;
    }

    public void setQtdeContas(int qtdeContas) {
        this.qtdeContas = qtdeContas;
    }
}
