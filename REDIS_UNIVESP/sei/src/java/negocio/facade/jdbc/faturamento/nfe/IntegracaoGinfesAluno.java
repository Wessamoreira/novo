package negocio.facade.jdbc.faturamento.nfe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.faturamento.nfe.IntegracaoGinfesAlunoItemVO;
import negocio.comuns.faturamento.nfe.IntegracaoGinfesAlunoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisExcel;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.faturamento.nfe.IntegracaoGinfesAlunoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class IntegracaoGinfesAluno extends ControleAcesso implements IntegracaoGinfesAlunoInterfaceFacade {

	private static final long serialVersionUID = -1L;
	protected static String idEntidade = "IntegracaoGinfesAluno";

	public IntegracaoGinfesAluno() {
		super();
	}

	public void validarDados(IntegracaoGinfesAlunoVO obj) throws ConsistirException {
		if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo UNIDADE ENSINO (IntegracaoGinfesAluno) deve ser informado.");
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(IntegracaoGinfesAlunoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);
		if (obj.getCodigo() == 0) {
			incluir(obj, verificarAcesso, usuarioVO);
		} else {
			alterar(obj, verificarAcesso, usuarioVO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final IntegracaoGinfesAlunoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			IntegracaoGinfesAluno.incluir(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO IntegracaoGinfesAluno (unidadeensino, anoReferencia, mesReferencia,descontocondicional,descontoincondicional , situacaoContaReceber) VALUES (?, ?, ?, ?, ? , ?)");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getUnidadeEnsino(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getAnoReferencia(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getMesReferencia(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDescontoCondicional(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDescontoIncondicional(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getSituacaoContaReceber(), ++i, sqlInserir);
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			getFacadeFactory().getIntegracaoGinfesAlunoItemFacade().incluirAlunos(obj, obj.getAlunos(), usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final IntegracaoGinfesAlunoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			if (obj.getImportado()) {
				throw new Exception("Este cadastro não pode ser alterado pois já foi marcado como importado!");
			}
			IntegracaoGinfesAluno.alterar(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE IntegracaoGinfesAluno SET unidadeensino=?, anoReferencia=?, mesReferencia=? ,descontocondicional=?, descontoincondicional =? , situacaoContaReceber=?  WHERE codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getUnidadeEnsino(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getAnoReferencia(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getMesReferencia(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getDescontoCondicional(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getDescontoIncondicional(), ++i, sqlAlterar);					
					Uteis.setValuePreparedStatement(obj.getSituacaoContaReceber(), ++i, sqlAlterar);					
					Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
					return sqlAlterar;
				}
			}) == 0) {
				incluir(obj, false, usuario);
			}
			getFacadeFactory().getIntegracaoGinfesAlunoItemFacade().excluirAlunos(obj, usuario);
			getFacadeFactory().getIntegracaoGinfesAlunoItemFacade().incluirAlunos(obj, obj.getAlunos(), usuario);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void importar(final IntegracaoGinfesAlunoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			IntegracaoGinfesAluno.alterar(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE IntegracaoGinfesAluno SET dataImportacao = ?, importado = ? WHERE codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getDataImportacao(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getImportado(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
					return sqlAlterar;
				}
			}) == 0) {
				throw new Exception("Falha ao definir arquivo como importado!");
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(IntegracaoGinfesAlunoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			if (obj.getImportado()) {
				throw new Exception("Este cadastro não pode ser excluido pois já foi marcado como importado!");
			}
			IntegracaoGinfesAluno.excluir(getIdEntidade(), verificarAcesso, usuario);
			String sql = "DELETE FROM IntegracaoGinfesAluno WHERE codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<IntegracaoGinfesAlunoVO> consultar(Integer unidadeEnsino, String anoReferencia, String mesReferencia, Boolean naoImportados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT * FROM IntegracaoGinfesAluno WHERE 1=1");
		if (unidadeEnsino > 0) {
			sqlStr.append(" AND unidadeensino = ").append(unidadeEnsino);
		}
		if (naoImportados) {
			sqlStr.append(" AND dataimportacao is null");
		}
		sqlStr.append(" AND anoreferencia = '").append(anoReferencia).append("'");
		sqlStr.append(" AND mesreferencia = '").append(mesReferencia).append("'");
		sqlStr.append(" ORDER BY dataimportacao");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public IntegracaoGinfesAlunoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		try {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = new StringBuilder("select * from IntegracaoGinfesAluno where codigo = ?");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigoPrm);
			if (!tabelaResultado.next()) {
				throw new StreamSeiException("Dados Não Encontrados ( IntegracaoGinfesAlunoVO ).");
			}
			IntegracaoGinfesAlunoVO obj = new IntegracaoGinfesAlunoVO();
			montarDados(obj, tabelaResultado, nivelMontarDados, usuario);
			return obj;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	public String executarGeracaoArquivoImportacao(IntegracaoGinfesAlunoVO obj, int limite, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder("select codigo||(case when abreviatura is not null and trim(abreviatura) <> '' then '-'||trim(unaccent(abreviatura)) else '' end) arquivo, inscmunicipal im, regexp_replace(cnpj, '[^0-9]*', '', 'g') cnpj " + 
				"from unidadeensino where codigo = ").append(obj.getUnidadeEnsino().getCodigo());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			StringBuilder cabecalho = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?><importacaoAlunoEnvio xmlns=\"http://www.ginfes.com.br/tiposEducacao.xsd\">")
				.append("<empresaEscola><dsIm>").append(tabelaResultado.getString("im")).append("</dsIm><cpfCnpj>")
				.append(tabelaResultado.getString("cnpj")).append("</cpfCnpj></empresaEscola>");
			String rodape = "</importacaoAlunoEnvio>";
			StringBuilder arquivo = new StringBuilder();
			StringBuilder conteudo = new StringBuilder();
			Map<Integer, File> mapfile = new HashMap<Integer, File>(0);
			int parte = 1;
			int contador = 1;
			for (IntegracaoGinfesAlunoItemVO item : obj.getAlunos()) {
				if(item.getPossuiResponsavel().equals(1)) {
					if(!item.getNumeroIdentificacao().isEmpty()) {
						conteudo.append("<aluno><identificacao><numeroIdentificacao>").append(item.getNumeroIdentificacao())
						.append("</numeroIdentificacao><tipoIdentificacao>3</tipoIdentificacao><nome>").append(item.getNome());		
					}else if(!item.getNumeroIdentificacaoCPF().isEmpty()){
						conteudo.append("<aluno><identificacao><numeroIdentificacao>").append(item.getNumeroIdentificacaoCPF())
						.append("</numeroIdentificacao><tipoIdentificacao>1</tipoIdentificacao><nome>").append(item.getNome());
					}else {						
						conteudo.append("<aluno><identificacao><numeroIdentificacao>").append(item.getNumeroIdentificacaoCertidaoNascimento())
						.append("</numeroIdentificacao><tipoIdentificacao>6</tipoIdentificacao><nome>").append(item.getNome());		
					}
								
				}else {
					conteudo.append("<aluno><identificacao><numeroIdentificacao>").append(item.getNumeroIdentificacaoCPF())
					.append("</numeroIdentificacao><tipoIdentificacao>1</tipoIdentificacao><nome>").append(item.getNome());
				}				
				conteudo.append("</nome><matricula>").append(item.getMatricula()).append("</matricula><dataNascimento>")
					.append(item.getDataNascimento()).append("</dataNascimento><dataInicial>").append(item.getDataInicial())
					.append("</dataInicial><dataFinal>").append(item.getDataFinal()).append("</dataFinal></identificacao>")
					.append("<icResponsavel>").append(item.getPossuiResponsavel()).append("</icResponsavel>")
					.append("<icAtivo>").append(item.getAlunoAtivo()).append("</icAtivo>")
					.append("<contato><email>").append(item.getEmail()).append("</email></contato>");
					if(item.getPossuiResponsavel().equals(1)
							&& Uteis.isAtributoPreenchido(item.getCepResponsavel())
							&& Uteis.isAtributoPreenchido(item.getLogradouroResponsavel()) 
							&& Uteis.isAtributoPreenchido(item.getBairroResponsavel())
							&& Uteis.isAtributoPreenchido(item.getMunicipioResponsavel())
							&& Uteis.isAtributoPreenchido(item.getUfResponsavel())) {						
						conteudo.append("<endereco><cep>").append(item.getCepResponsavel()).append("</cep>")
						.append("<numero>").append(item.getNumeroResponsavel().trim().isEmpty()?"00":item.getNumeroResponsavel().trim()).append("</numero>")
						.append("<logradouro>").append(item.getLogradouroResponsavel()).append("</logradouro>")
						.append("<bairro>").append(item.getBairroResponsavel()).append("</bairro>")
						.append("<uf>").append(item.getUfResponsavel()).append("</uf>")
						.append("<municipio>").append(item.getMunicipioResponsavel()).append("</municipio></endereco>");//		
					}else {
						conteudo
						.append("<endereco><cep>").append(item.getCep()).append("</cep>")
						.append("<numero>").append(item.getNumero().trim()).append("</numero>")
						.append("<logradouro>").append(item.getLogradouro()).append("</logradouro>")
						.append("<bairro>").append(item.getBairro()).append("</bairro>")
						.append("<uf>").append(item.getUf()).append("</uf>")
						.append("<municipio>").append(item.getMunicipio()).append("</municipio></endereco>");//	if (item.getPossuiResponsavel().intValue() == 1) {	
					}
							
					conteudo.append("<responsavel><identificacao><numeroIdentificacao>").append(item.getNumeroIdentificacaoResponsavel())
						.append("</numeroIdentificacao><tipoIdentificacao>1</tipoIdentificacao><nome>").append(item.getNomeResponsavel())
						.append("</nome></identificacao>")
						.append("<contato><email>").append(item.getEmailResponsavel()).append("</email></contato>")
						.append("<endereco><cep>").append(item.getCepResponsavel()).append("</cep>")
						.append("<numero>").append(item.getNumeroResponsavel().trim().isEmpty()?"00":item.getNumeroResponsavel().trim()).append("</numero>")
						.append("<logradouro>").append(item.getLogradouroResponsavel()).append("</logradouro>")
						.append("<bairro>").append(item.getBairroResponsavel()).append("</bairro>")
						.append("<uf>").append(item.getUfResponsavel()).append("</uf>")
						.append("<municipio>").append(item.getMunicipioResponsavel()).append("</municipio></endereco></responsavel>");
//				} else {
//					conteudo.append("<responsavel/>");
//				}
				conteudo.append("<listaCursos><Curso>")
					.append("<codCurso>").append(item.getCodigoUnidadeEnsinoCurso()).append("</codCurso>")
					.append("<serie>").append(Uteis.removeCaractersEspeciais(item.getSerie())).append("</serie>")
					.append("<valores>")
					.append("<valorServico>").append(Uteis.retornarDuasCasasDecimais(String.valueOf(item.getValorServico()))).append("</valorServico>")					
					.append("<valorDescCondicionado>").append(obj.getDescontoCondicional() ? Uteis.retornarDuasCasasDecimais(String.valueOf(item.getValorDescontoCondicionado())) : "0.00").append("</valorDescCondicionado>")
					.append("<valorDescIncondicionado>").append(obj.getDescontoIncondicional() ? Uteis.retornarDuasCasasDecimais(String.valueOf(item.getValorDescontoIncondicionado())) : "0.00").append("</valorDescIncondicionado>")
//					.append("<valorDescEducacao>").append(Uteis.retornarDuasCasasDecimais(String.valueOf(item.getValorDesconto()))).append("</valorDescEducacao>")
//					.append("<dataInicioDescEducacao>").append(item.getDataInicioDesconto()).append("</dataInicioDescEducacao>")
//					.append("<dataFimDescEducacao>").append(item.getDataFimDesconto()).append("</dataFimDescEducacao>")
//					.append("<valorCustosDiversos>").append(Uteis.retornarDuasCasasDecimais(String.valueOf(item.getValorCustos()))).append("</valorCustosDiversos>")
//					.append("<dataInicioCustosDiversos>").append(item.getDataInicioCustos()).append("</dataInicioCustosDiversos>")
//					.append("<dataFimCustosDiversos>").append(item.getDataFimCustos()).append("</dataFimCustosDiversos>")
					.append("</valores>")
					.append("<tpCurso>1</tpCurso><dataInicioCurso>").append(item.getDataInicioCurso()).append("</dataInicioCurso>")
					.append("<icAtivo>").append(item.getCursoAtivo()).append("</icAtivo></Curso></listaCursos></aluno>");
				if (contador >= limite) {
					contador = 1;
					arquivo = new StringBuilder(cabecalho.toString()).append(conteudo.toString()).append(rodape);
					mapfile.put(parte, realizarGravacaoArquivoImportacao("aluno-" + tabelaResultado.getString("arquivo") + "-parte" + parte, arquivo.toString()));
					parte++;
					conteudo = new StringBuilder();
				} else {
					contador++;
				}
			}
			if (!conteudo.toString().isEmpty()) {
				arquivo = new StringBuilder(cabecalho.toString()).append(conteudo.toString()).append(rodape);
				mapfile.put(parte, realizarGravacaoArquivoImportacao("aluno-" + tabelaResultado.getString("arquivo") + "-parte" + parte, arquivo.toString()));
			}
			String caminhoZip = getCaminhoPastaWeb() + File.separator + "relatorio" + File.separator + "aluno-" + tabelaResultado.getString("arquivo") + "-" + Uteis.getData(new Date(), "dd-MM-yyyy") + ".zip";
			File arquivoZip = new File(caminhoZip);
			File[] files = new File[mapfile.size()];
			int y = 0;
			for (File file : mapfile.values()) {
				files[y++] = file;
			}
			getFacadeFactory().getArquivoHelper().zip(files, arquivoZip);
			return "aluno-" + tabelaResultado.getString("arquivo") + "-" + Uteis.getData(new Date(), "dd-MM-yyyy") + ".zip";
		} else {
			throw new Exception("Unidade de Ensino não encontrada!");
		}
	}
	
	private File realizarGravacaoArquivoImportacao(String nome, String conteudo) throws Exception {
		String caminho = getCaminhoPastaWeb() + File.separator + "relatorio" + File.separator + nome + "-" + Uteis.getData(new Date(), "dd-MM-yyyy") + ".xml";
		File xml = new File(caminho);
		if (xml.exists()) {
			try {
				xml.delete();
			} catch (Exception e) {
				throw new Exception("Erro ao excluir arquivo de importação Ginfes.");
			}
		}
		PrintWriter out = new PrintWriter(xml);
		try {
			out.println(conteudo);
		} catch (Exception e) {
			throw new Exception("Erro ao gravar arquivo de importação Ginfes.");
		} finally {
			out.flush();
			out.close();
		}
		return xml;
	}

	
	@Override
	public File realizarGeracaoRelatorioExcelIntegracaoGinfesAluno(IntegracaoGinfesAlunoVO integracaoGinfesAlunoVO, String urlLogoPadraoRelatorio,  boolean isAlunosComErro, UsuarioVO usuario) throws Exception {
		File arquivo = null;
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(isAlunosComErro ? "Importação Ginfes - Aluno Erros": "Importação Ginfes - Aluno");
		UteisExcel uteisExcel = new UteisExcel(workbook);
		uteisExcel.realizarGeracaoTopoPadraoRelatorio(workbook, sheet, urlLogoPadraoRelatorio, null , isAlunosComErro ? 15:14, "");
		montarCabecalhoRelatorioExcelIntegracaoGinfesAluno(uteisExcel, workbook, sheet, isAlunosComErro);
		montarConteudoRelatorioExcelIntegracaoGinfesAluno(uteisExcel, workbook, sheet, integracaoGinfesAlunoVO, isAlunosComErro, usuario);
		arquivo = new File(getCaminhoPastaWeb() + File.separator + "relatorio" + File.separator + String.valueOf(new Date().getTime())+".xls");
		FileOutputStream out = new FileOutputStream(arquivo);
		workbook.write(out);
		out.close();
		return arquivo;
	}
	
	private void montarCabecalhoRelatorioExcelIntegracaoGinfesAluno(UteisExcel uteisExcel, HSSFWorkbook workbook, HSSFSheet sheet, boolean isAlunosComErro) {
		int cellnum = 0;	
		Row row = sheet.createRow(sheet.getLastRowNum() + 1);
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 5000, "CPF Responsável");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 10000,"Nome Responsável");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 5000,"RG Aluno");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 10000,"Nome Aluno");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 5000,"Matrícula");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 10000,"Curso");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 10000,"Turno");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 5000,"Código Curso Ginfes");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 4000,"Parcela");		
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 4000,"Série");		
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 4000,"Ativo");		
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 4000,"Valor Serviço");		
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 6000,"Desconto Condicionado");		
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 6000,"Desconto Incondicionado");		
		if(isAlunosComErro) {
			uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 10000,"Motivo Erro");	
		}
				
				
	}
	
	private void montarConteudoRelatorioExcelIntegracaoGinfesAluno(UteisExcel uteisExcel, HSSFWorkbook workbook, HSSFSheet sheet, IntegracaoGinfesAlunoVO integracaoGinfesAlunoVO, boolean isAlunosComErro,  UsuarioVO usuario) throws Exception {
		List<IntegracaoGinfesAlunoItemVO> lista = isAlunosComErro ?  integracaoGinfesAlunoVO.getAlunosErro() : integracaoGinfesAlunoVO.getAlunos();
		Row row = null;
		for (IntegracaoGinfesAlunoItemVO igai : lista) {
			row = sheet.createRow(sheet.getLastRowNum() + 1);
			int cellnum = 0;
			uteisExcel.preencherCelula( row, cellnum++,igai.getNumeroIdentificacaoResponsavel());
			uteisExcel.preencherCelula( row, cellnum++,igai.getNomeResponsavel());
			uteisExcel.preencherCelula( row, cellnum++,igai.getNumeroIdentificacao());
			uteisExcel.preencherCelula( row, cellnum++,igai.getNome());
			uteisExcel.preencherCelula( row, cellnum++,igai.getMatricula());
			uteisExcel.preencherCelula( row, cellnum++,igai.getNomeCurso());
			uteisExcel.preencherCelula( row, cellnum++,igai.getNomeTurno());
			uteisExcel.preencherCelula( row, cellnum++,igai.getCodigoUnidadeEnsinoCurso());
			uteisExcel.preencherCelula( row, cellnum++,igai.getParcelaContaReceber());
			uteisExcel.preencherCelula( row, cellnum++,igai.getSerie());
			uteisExcel.preencherCelula( row, cellnum++,igai.getAlunoAtivo());
			uteisExcel.preencherCelula( row, cellnum++,igai.getValorServico());
			uteisExcel.preencherCelula( row, cellnum++,igai.getValorDescontoCondicionado());
			uteisExcel.preencherCelula( row, cellnum++,igai.getValorDescontoIncondicionado());
			if(isAlunosComErro) {
				uteisExcel.preencherCelula( row, cellnum++,igai.getErroValidacao());	
			}
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private List<IntegracaoGinfesAlunoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<IntegracaoGinfesAlunoVO> vetResultado = new ArrayList<IntegracaoGinfesAlunoVO>(0);
		while (tabelaResultado.next()) {
			IntegracaoGinfesAlunoVO obj = new IntegracaoGinfesAlunoVO();
			montarDados(obj, tabelaResultado, nivelMontarDados, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}
	
	private void montarDados(IntegracaoGinfesAlunoVO obj, SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo((dadosSQL.getInt("codigo")));
		obj.setDataImportacao(dadosSQL.getTimestamp("dataimportacao"));
		obj.setAnoReferencia(dadosSQL.getString("anoreferencia"));
		obj.setMesReferencia(dadosSQL.getString("mesreferencia"));
		obj.setImportado(dadosSQL.getBoolean("importado"));
		obj.setDescontoCondicional(dadosSQL.getBoolean("descontocondicional"));
		obj.setDescontoIncondicional(dadosSQL.getBoolean("descontoincondicional"));
		obj.setSituacaoContaReceber(dadosSQL.getString("situacaoContaReceber"));		
		obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(dadosSQL.getInt("unidadeensino"), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
		obj.setAlunos(getFacadeFactory().getIntegracaoGinfesAlunoItemFacade().consultarPorIntegracao(obj.getCodigo(), usuario));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			return;
		}
	}

	public static String getIdEntidade() {
		return IntegracaoGinfesAluno.idEntidade;
	}

}
