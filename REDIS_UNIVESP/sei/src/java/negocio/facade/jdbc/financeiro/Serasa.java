package negocio.facade.jdbc.financeiro;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.FiliacaoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.SerasaVO;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.Comando;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.EditorOC;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.SerasaInterfaceFacade;
import relatorio.negocio.comuns.financeiro.SerasaRelVO;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>SerasaVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>SerasaVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see SerasaVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class Serasa extends ControleAcesso implements SerasaInterfaceFacade {

	private List<ContaReceberVO> contaReceberVOs;
	private ArquivoHelper arquivoHelper = new ArquivoHelper();
	private PrintWriter printWriter;
	protected static String idEntidade;

	public Serasa() throws Exception {
		super();
		setIdEntidade("Serasa");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.SerasaInterfaceFacade#novo()
	 */
	public SerasaVO novo() throws Exception {
		Serasa.incluir(getIdEntidade());
		SerasaVO obj = new SerasaVO();
		return obj;
	}

	public void validarDados(SerasaVO serasaVO) throws Exception {
		if (serasaVO.getTelefoneInstituicao().equals("")) {
			throw new ConsistirException("Informe o Telefone da Instituição");
		}
		if (serasaVO.getRamal().equals("")) {
			throw new ConsistirException("Informe o Ramal do telefone da Instituição");
		}
		if (serasaVO.getContatoInstituicao().equals("")) {
			throw new ConsistirException("Informe o Contato na Instituição");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.SerasaInterfaceFacade#incluir(negocio.comuns.financeiro.SerasaVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final SerasaVO obj) throws Exception {
		try {
			Serasa.incluir(getIdEntidade());
			final String sql = "INSERT INTO Serasa ( responsavel, dataGeracao, arquivoSerasa, unidadeEnsino, logon, contatoInstituicao, " + "telefoneInstituicao, ramal ) "
					+ "VALUES ( ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					if (obj.getResponsavel().getCodigo().intValue() != 0) {
						sqlInserir.setInt(1, obj.getResponsavel().getCodigo().intValue());
					} else {
						sqlInserir.setNull(1, 0);
					}
					sqlInserir.setDate(2, Uteis.getDataJDBC(obj.getDataGeracao()));
					if (obj.getArquivoSerasa().getCodigo().intValue() != 0) {
						sqlInserir.setInt(3, obj.getArquivoSerasa().getCodigo().intValue());
					} else {
						sqlInserir.setNull(3, 0);
					}
					if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
						sqlInserir.setInt(4, obj.getUnidadeEnsino().getCodigo().intValue());
					} else {
						sqlInserir.setNull(4, 0);
					}
					sqlInserir.setString(5, obj.getLogon());
					sqlInserir.setString(6, obj.getContatoInstituicao());
					sqlInserir.setString(7, obj.getTelefoneInstituicao());
					sqlInserir.setString(8, obj.getRamal());
					return sqlInserir;
				}
			}, new ResultSetExtractor() {

				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.SerasaInterfaceFacade#excluir(negocio.comuns.financeiro.SerasaVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(SerasaVO obj) throws Exception {
		try {
			Serasa.excluir(getIdEntidade());
			String sql = "DELETE FROM Serasa WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.SerasaInterfaceFacade#consultarPorResponsavel(java.lang.Integer, boolean, int)
	 */
	public List consultarPorResponsavel(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Serasa WHERE responsavel = " + valorConsulta.intValue() + "";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.SerasaInterfaceFacade#consultarPorCodigo(java.lang.Integer, boolean, int)
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Serasa WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho
	 * para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>SerasaVO</code> resultantes da consulta.
	 */
	public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe <code>SerasaVO</code>.
	 * 
	 * @return O objeto da classe <code>SerasaVO</code> com os dados devidamente montados.
	 */
	public static SerasaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		SerasaVO obj = new SerasaVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getResponsavel().setCodigo(dadosSQL.getInt("responsavel"));
		obj.setDataGeracao(dadosSQL.getDate("dataGeracao"));
		obj.getArquivoSerasa().setCodigo(new Integer(dadosSQL.getInt("arquivoSerasa")));
		obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino")));
		obj.setLogon(dadosSQL.getString("logon"));
		obj.setContatoInstituicao(dadosSQL.getString("contatoInstituicao"));
		obj.setTelefoneInstituicao(dadosSQL.getString("telefoneInstituicao"));
		obj.setRamal(dadosSQL.getString("ramal"));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		montarDadosArquivo(obj, nivelMontarDados, usuario);
		montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosUnidadeEnsino(obj, nivelMontarDados, usuario);
		return obj;
	}

	public static void montarDadosArquivo(SerasaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getArquivoSerasa().getCodigo().intValue() == 0) {
			obj.setArquivoSerasa(new ArquivoVO());
			return;
		}
		obj.setArquivoSerasa(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getArquivoSerasa().getCodigo(), nivelMontarDados, usuario));
	}

	public static void montarDadosResponsavel(SerasaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavel().getCodigo().intValue() == 0) {
			obj.setResponsavel(new UsuarioVO());
			return;
		}
		obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados, usuario));
	}

	public static void montarDadosUnidadeEnsino(SerasaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
			obj.setUnidadeEnsino(new UnidadeEnsinoVO());
			return;
		}
		obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuario));
	}

	public List consultarPorDataGeracao(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Serasa WHERE ((dataGeracao >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataGeracao <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataGeracao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.SerasaInterfaceFacade#consultarPorChavePrimaria(java.lang.Integer, boolean, int)
	 */
	public SerasaVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sql = "SELECT * FROM Serasa WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( Serasa ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return Serasa.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos.
	 * Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		Serasa.idEntidade = idEntidade;
	}

	// Registro Tipo 0 (Zero) : Registro Header do Arquivo.
	public void gerarDadosRegistroTipoZero(EditorOC editorOC, Comando cmd, SerasaVO serasaVO, int numeroSequencial) throws Exception {
		String linha = "";
		// Código do Registro = 0
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 1, 1, " ", false, false);
		// CNPJ da Instituição
		String cnpj = serasaVO.getUnidadeEnsino().getCNPJ().substring(0, serasaVO.getUnidadeEnsino().getCNPJ().indexOf("/"));
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(cnpj), 2, 10, "0", false, false);
		// Data da geração do arquivo
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getDataAplicandoFormatacao(new Date(), "yyyyMMdd"), 11, 18, " ", false, false);
		// N° DDD do telefone de contato da instituição
		// Nº telefone contato da instituição informante
		String ddd = serasaVO.getTelefoneInstituicao().substring(serasaVO.getTelefoneInstituicao().indexOf("(") + 1, serasaVO.getTelefoneInstituicao().indexOf(")"));
		String telefone = serasaVO.getTelefoneInstituicao().substring(serasaVO.getTelefoneInstituicao().indexOf(")") + 1, serasaVO.getTelefoneInstituicao().length());
		linha = editorOC.adicionarCampoLinhaVersao2(linha, ddd, 19, 22, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removeCaractersEspeciais(telefone), 23, 30, " ", false, false);
		// N° ramal do telefone da instituição
		linha = editorOC.adicionarCampoLinhaVersao2(linha, serasaVO.getRamal(), 31, 34, "0", false, false);
		// Nome do contato da instituição informante
		linha = editorOC.adicionarCampoLinhaVersao2(linha, serasaVO.getContatoInstituicao(), 35, 104, " ", true, false);
		// Identificação do arquivo fixo = SERASA-CONVEM04
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "SERASA-CONVEM04", 105, 119, " ", true, false);
		// Número sequencial
		linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(numeroSequencial), 120, 125, "0", false, false);
		// Código de Envio do arquivo = E -> Entrada R -> Retorno
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "E", 126, 126, " ", false, false);
		// Diferencial de remessa
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 127, 130, " ", true, false);
		// Espaços em Branco
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 131, 133, " ", true, false);
		// Informar o LOGON a ser usado
		linha = editorOC.adicionarCampoLinhaVersao2(linha, serasaVO.getLogon(), 134, 141, " ", false, false);
		// Espaços em Branco
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 142, 533, " ", false, false);
		// Código de Erros
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 534, 593, " ", true, false);
		// Sequencia do registro no arquivo
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "0000001", 594, 600, " ", true, false);
		numeroSequencial++;
		cmd.adicionarLinhaComando(linha, 0);
	}

	// Registro Tipo 1 : registro detalhe do arquivo com os dados do devedor e da anotação a ser incluída no banco de dados do PEFIN.
	public void gerarDadosRegistroTipoUm(EditorOC editorOC, Comando cmd, SerasaVO serasaVO, SerasaRelVO serasaRelVO, PessoaVO pessoaVO, Date dataVencimento, int numeroSequencial) throws Exception {
		String linha = "";
		// Código do registro = 1
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 1, 1, " ", false, false);
		// Código da operação = I -> Inclusão E -> Exclusão
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "I", 2, 2, " ", false, false);
		// Filial e dígito do CNPJ da contratante
		String filialDigito = serasaVO.getUnidadeEnsino().getCNPJ().substring(serasaVO.getUnidadeEnsino().getCNPJ().indexOf("/") + 1, serasaVO.getUnidadeEnsino().getCNPJ().length());
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(filialDigito), 3, 8, " ", false, false);
		// Data da ocorrência
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getDataAplicandoFormatacao(dataVencimento, "yyyyMMdd"), 9, 16, " ", false, false);
		// Data do término do contrato
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getDataAplicandoFormatacao(dataVencimento, "yyyyMMdd"), 17, 24, " ", false, false);
		// Código de natureza da operação
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "ME", 25, 27, " ", false, false);
		// Código da Praça Embratel
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 28, 31, " ", false, false);
		// Tipo de pessoa do principal - Física -> F Jurídica -> J
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "F", 32, 32, " ", false, false);
		// Tipo do primeiro documento - CNPJ -> 1 CPF -> 2
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "2", 33, 33, " ", true, false);
		// Primeiro documento do principal
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(pessoaVO.getCPF()), 34, 48, "0", false, false);
		// Motivo da Baixa
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 49, 50, " ", false, false);
		// Tipo do segudo documento do principal - RG -> 3
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "3", 51, 51, " ", false, false);
		// Segundo Documento do principal
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(pessoaVO.getRG()), 52, 66, " ", false, false);
		// UF quando documento for RG
		linha = editorOC.adicionarCampoLinhaVersao2(linha, pessoaVO.getEstadoEmissaoRG(), 67, 68, " ", false, false);
		// Campos em branco devido ao documento se referir somente ao principal, e não aos seus coobrigados.
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 69, 105, " ", false, false);
		// Nome do devedor
		linha = editorOC.adicionarCampoLinhaVersao2(linha, pessoaVO.getNome(), 106, 175, " ", true, false);
		// Data do nascimento do devedor
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getDataAplicandoFormatacao(pessoaVO.getDataNasc(), "yyyyMMdd"), 176, 183, " ", false, false);
		// Nome do Pai
		String pai = "";
		String mae = "";
		for (FiliacaoVO filiacaoVO : pessoaVO.getFiliacaoVOs()) {
			if (filiacaoVO.getTipo().equals("PA")) {
				pai = filiacaoVO.getNome();
			} else if (filiacaoVO.getTipo().equals("MA")) {
				mae = filiacaoVO.getNome();
			}
		}
		linha = editorOC.adicionarCampoLinhaVersao2(linha, pai, 184, 253, " ", true, false);
		// Nome da Mãe
		linha = editorOC.adicionarCampoLinhaVersao2(linha, mae, 254, 323, " ", true, false);
		// Endereço Completo
		linha = editorOC.adicionarCampoLinhaVersao2(linha, pessoaVO.getEndereco(), 324, 368, " ", true, false);
		// Bairro Correspondente
		linha = editorOC.adicionarCampoLinhaVersao2(linha, pessoaVO.getSetor(), 369, 388, " ", true, false);
		// Município correspondente
		linha = editorOC.adicionarCampoLinhaVersao2(linha, pessoaVO.getCidade().getNome(), 389, 413, " ", true, false);
		// Sigla Unidade Federativa
		linha = editorOC.adicionarCampoLinhaVersao2(linha, pessoaVO.getCidade().getEstado().getSigla(), 414, 415, " ", true, false);
		// CEP (Código Endereçamento Postal)
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(pessoaVO.getCEP()), 416, 423, " ", true, false);
		// Valor da conta, com 2 decimais alinhado a direita com zeros a esquerda
		String valorTotal = String.valueOf(Uteis.formatarDecimalDuasCasas(serasaRelVO.getValorTotal()));
		valorTotal = Uteis.removeCaractersEspeciais(valorTotal);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, valorTotal, 424, 438, "0", false, false);
		// N° do contrato
		linha = editorOC.adicionarCampoLinhaVersao2(linha, serasaRelVO.getMatriculaAluno(), 439, 454, " ", false, false);
		// Nosso Número
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 455, 463, " ", false, false);
		// Complemento do endereço do devedor
		linha = editorOC.adicionarCampoLinhaVersao2(linha, pessoaVO.getComplemento(), 464, 488, " ", false, false);
		// DDD do telefone do devedor
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "XX", 489, 492, " ", false, false);
		// Telefone do devedor
		linha = editorOC.adicionarCampoLinhaVersao2(linha, pessoaVO.getTelefoneRes(), 493, 501, " ", false, false);
		// Data do compromisso assumida pelo devedor
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 502, 509, " ", false, false);
		// Valor total do compromisso assumido pelo devedor
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 510, 524, "0", false, false);
		// Branco
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 525, 533, " ", false, false);
		// Código de 3 erros
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 534, 593, " ", false, false);
		// Número sequencial
		linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(numeroSequencial), 594, 600, "0", false, false);
		cmd.adicionarLinhaComando(linha, 0);
	}

	// Registro tipo 9 - Registro trailler do arquivo
	public void gerarDadosRegistroTipoNove(EditorOC editorOC, Comando cmd, int numeroSequencial) throws Exception {
		String linha = "";
		// Código do registro = 9
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "9", 1, 1, " ", false, false);
		// Espaços em branco
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 2, 593, " ", true, false);
		// Número sequencial
		linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(numeroSequencial), 594, 600, "0", false, false);
		cmd.adicionarLinhaComando(linha, 0);
	}

	public void gerarDadosArquivoSerasa(SerasaVO serasaVO, List<SerasaRelVO> serasaRelVOs, String caminhoPasta, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		
		String nomeArquivo = "SERASA_" + Uteis.getData(new Date(), "ddMMyyyy") + ".txt";
		executarCriarArquivoTexto(caminhoPasta + File.separator +PastaBaseArquivoEnum.SERASA_TMP.getValue(), nomeArquivo);
		
		EditorOC editorOC = new EditorOC();
		Comando cmd = new Comando();
		int numeroSequencial = 1;

//		ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario, null);
//		ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario, null);
		serasaVO.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(serasaVO.getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));

		gerarDadosRegistroTipoZero(editorOC, cmd, serasaVO, numeroSequencial);

		Iterator i = serasaRelVOs.iterator();
		while (i.hasNext()) {
			numeroSequencial++;
			SerasaRelVO serasaRelVO = (SerasaRelVO) i.next();
			PessoaVO pessoaVO = getFacadeFactory().getPessoaFacade().consultarAlunoPorMatricula(serasaRelVO.getMatriculaAluno(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			setContaReceberVOs(getFacadeFactory().getContaReceberFacade().consultaRapidaPorMatriculaParaSerasa(serasaRelVO.getMatriculaAluno(), configuracaoFinanceiroVO));
			Date dataVencimento = getContaReceberVOs().get(0).getDataVencimento();
			gerarDadosRegistroTipoUm(editorOC, cmd, serasaVO, serasaRelVO, pessoaVO, dataVencimento, numeroSequencial);
		}
		numeroSequencial++;
		gerarDadosRegistroTipoNove(editorOC, cmd, numeroSequencial);

		editorOC.adicionarComando(cmd);

		getPrintWriter().print(editorOC.getText());
		getPrintWriter().close();

		serasaVO.getArquivoSerasa().setNome(nomeArquivo);
		serasaVO.getArquivoSerasa().setOrigem(OrigemArquivo.SERASA.getValor());
//		serasaVO.getArquivoSerasa().setCodOrigem(serasaVO.getCodigo());

		serasaVO.getArquivoSerasa().setPastaBaseArquivo(PastaBaseArquivoEnum.SERASA_TMP.getValue());
		serasaVO.getArquivoSerasa().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.SERASA_TMP);

		// serasaVO.getArquivoSerasa().setArquivo(arquivoHelper.getArray(arquivo));

		getFacadeFactory().getArquivoFacade().incluir(serasaVO.getArquivoSerasa(),usuario, configuracaoGeralSistemaVO );
		incluir(serasaVO);
	}

	private File executarCriarArquivoTexto(String caminhoPasta, String nomeArquivo) throws Exception {
		setPrintWriter(arquivoHelper.criarArquivoTexto(caminhoPasta, nomeArquivo, true));
		return new File(caminhoPasta + File.separator + nomeArquivo);
	}

	public void setPrintWriter(PrintWriter printWriter) {
		this.printWriter = printWriter;
	}

	public PrintWriter getPrintWriter() {
		return printWriter;
	}

	public void setContaReceberVOs(List<ContaReceberVO> contaReceberVOs) {
		this.contaReceberVOs = contaReceberVOs;
	}

	public List<ContaReceberVO> getContaReceberVOs() {
		if (contaReceberVOs == null) {
			contaReceberVOs = new ArrayList<ContaReceberVO>(0);
		}
		return contaReceberVOs;
	}

   
}
