package negocio.facade.jdbc.protocolo;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.richfaces.event.FileUploadEvent;
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

import negocio.comuns.academico.CidTipoRequerimentoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.protocolo.TipoRequerimentoVO;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoArquivo;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.protocolo.CidTipoRequerimentoInterfaceFacade;


@Repository
@Scope("singleton")
@Lazy
public class CidTipoRequerimento extends ControleAcesso implements CidTipoRequerimentoInterfaceFacade {
	
	private static final long serialVersionUID = 2563675958470104185L;
	protected static String idEntidade;

	public CidTipoRequerimento() throws Exception {
		super();
		setIdEntidade("cidtiporequerimento");
	}
	
	
	public static String getIdEntidade() {
		return CidTipoRequerimento.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		CidTipoRequerimento.idEntidade = idEntidade;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final CidTipoRequerimentoVO obj, UsuarioVO usuario) throws Exception {
//		TipoRequerimentoDepartamentoVO.validarDados(obj);

		final String sql = "INSERT INTO cidtiporequerimento( " + "codcid, descricao, tiporequerimento) VALUES ( ? , ?, ? ) returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				int i = 1;
				sqlInserir.setString(i++, obj.getCodCid());
				sqlInserir.setString(i++, obj.getDescricaoCid());
				sqlInserir.setInt(i++, obj.getTipoRequerimento());
				return sqlInserir;
			}
		}, new ResultSetExtractor<Integer>() {

			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if (arg0.next()) {
					obj.setNovoObj(Boolean.FALSE);
					return arg0.getInt("codigo");
				}
				return null;
			}
		}));
		obj.setNovoObj(Boolean.FALSE);
	}
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final CidTipoRequerimentoVO obj, UsuarioVO usuario) throws Exception {
//		CidTipoRequerimentoVO.validarDados(obj);
		final String sql = "UPDATE cidtiporequerimento SET " + "codcid = ?, descricao=?, tiporequerimento=?  WHERE (codigo = ?)"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		if(getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);

				int i = 1;
				sqlAlterar.setString(i++, obj.getCodCid());
				sqlAlterar.setString(i++, obj.getDescricaoCid());
				sqlAlterar.setInt(i++, obj.getTipoRequerimento().intValue());
				sqlAlterar.setInt(i++, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		}) ==0){
			incluir(obj, usuario);
			return;
		};
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(CidTipoRequerimentoVO obj, UsuarioVO usuario) throws Exception {		
		String sql = "DELETE FROM cidtiporequerimento WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirCidTipoRequerimentoVOs(Integer tipoRequerimento, List<CidTipoRequerimentoVO> objetos, UsuarioVO usuario) throws Exception {
		Iterator<CidTipoRequerimentoVO> e = objetos.iterator();
		while (e.hasNext()) {
			CidTipoRequerimentoVO obj = (CidTipoRequerimentoVO) e.next();
			obj.setTipoRequerimento(tipoRequerimento);
			incluir(obj, usuario);
		}
	}

	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarCidTipoRequerimentoVOs(Integer tipoRequerimento, List<CidTipoRequerimentoVO> objetos, UsuarioVO usuario) throws Exception {
		excluirCidTipoRequerimentoVOs(tipoRequerimento, objetos, usuario);
		for(CidTipoRequerimentoVO obj : objetos){
			obj.setTipoRequerimento(tipoRequerimento);
			if(!Uteis.isAtributoPreenchido(obj.getCodigo()) && obj.getNovoObj()){
				incluir(obj, usuario);
			}else{
				alterar(obj, usuario);
			}
		}

	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirCidTipoRequerimentoVOs(Integer tipoRequerimento, List<CidTipoRequerimentoVO> objetos,  UsuarioVO usuario) throws Exception {
		String sql = "DELETE FROM cidtiporequerimento WHERE (tipoRequerimento = ?) and codigo not in (0 ";
		for(CidTipoRequerimentoVO obj:objetos){
			sql += ", "+obj.getCodigo();
		}
		sql += ") "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { tipoRequerimento });
	}

	public  List<CidTipoRequerimentoVO> consultarCidPorTipoRequerimento(TipoRequerimentoVO tipoRequerimentoVO, UsuarioVO usuario) throws Exception {
		String sqlStr = "select * from cidtiporequerimento WHERE tiporequerimento = ?";
		 SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, tipoRequerimentoVO.getCodigo());
		 List<CidTipoRequerimentoVO> listaCidTipoRequerimentoVOs = new ArrayList<>(0);
	    	while (tabelaResultado.next()) {
	    		CidTipoRequerimentoVO obj = new CidTipoRequerimentoVO();
	    		obj.setCodCid(tabelaResultado.getString("codcid"));
	    		obj.setDescricaoCid(tabelaResultado.getString("descricao"));
	    		obj.setCodigo(tabelaResultado.getInt("codigo"));
	    		obj.setTipoRequerimento(tabelaResultado.getInt("tiporequerimento"));
	    		obj.setNovoObj(false);
	    		listaCidTipoRequerimentoVOs.add(obj);
	    	}
		return listaCidTipoRequerimentoVOs;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void inicializarDadosArquivoImportarCid(CidTipoRequerimentoVO cidTipoRequerimentoVO, FileUploadEvent fileUploadEvent, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		cidTipoRequerimentoVO.getArquivoVO().setNome(ArquivoHelper.criarNomeArquivo(usuarioVO, ArquivoHelper.getExtensaoArquivo(fileUploadEvent.getUploadedFile().getName())));
		cidTipoRequerimentoVO.getArquivoVO().setExtensao(ArquivoHelper.getExtensaoArquivo(fileUploadEvent.getUploadedFile().getName()));
		cidTipoRequerimentoVO.getArquivoVO().setDescricao(fileUploadEvent.getUploadedFile().getName());
		cidTipoRequerimentoVO.getArquivoVO().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.ARQUIVO_IMPORTACAO_CID_TMP);
		cidTipoRequerimentoVO.getArquivoVO().setPastaBaseArquivo(PastaBaseArquivoEnum.ARQUIVO_IMPORTACAO_CID_TMP.getValue());
		cidTipoRequerimentoVO.getArquivoVO().getResponsavelUpload().setCodigo(usuarioVO.getCodigo());
		cidTipoRequerimentoVO.getArquivoVO().getResponsavelUpload().setNome(usuarioVO.getNome());
		cidTipoRequerimentoVO.getArquivoVO().setDataUpload(new Date());
		cidTipoRequerimentoVO.getArquivoVO().setManterDisponibilizacao(true);
		cidTipoRequerimentoVO.getArquivoVO().setDataDisponibilizacao(new Date());
		cidTipoRequerimentoVO.getArquivoVO().setDataIndisponibilizacao(null);
		cidTipoRequerimentoVO.getArquivoVO().setSituacao(SituacaoArquivo.ATIVO.getValor());
		cidTipoRequerimentoVO.getArquivoVO().setOrigem(OrigemArquivo.IMPORTACAO_CID.getValor());
		ArquivoHelper.salvarArquivoNaPastaTemp(fileUploadEvent, cidTipoRequerimentoVO.getArquivoVO().getNome(), cidTipoRequerimentoVO.getArquivoVO().getPastaBaseArquivo(), configuracaoGeralSistemaVO, usuarioVO);
		
	}
	
	public Cell getValorCelula(int numeroCelula, Row row, Boolean isString) {
		Cell cell = row.getCell(numeroCelula);
		if (cell != null && isString) {
			cell.setCellType(Cell.CELL_TYPE_STRING);
		}
		return cell;
	}
	
	
	 @Override
		public List<CidTipoRequerimentoVO> realizarProcessamentoExcelCid(FileUploadEvent uploadEvent,  CidTipoRequerimentoVO cidTipoRequerimentoVO, List<CidTipoRequerimentoVO> cidTipoRequerimentoVOs, Boolean inicializarDadosArquivo, ProgressBarVO progressBarVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception {
			return realizarProcessamentoExcelImportarCid(uploadEvent, cidTipoRequerimentoVO, cidTipoRequerimentoVOs, inicializarDadosArquivo, progressBarVO, configuracaoGeralSistemaVO, usuario);
		}
	 
	 
	 public List<CidTipoRequerimentoVO> realizarProcessamentoExcelImportarCid(FileUploadEvent uploadEvent, CidTipoRequerimentoVO cidTipoRequerimentoVO, List<CidTipoRequerimentoVO> cidTipoRequerimentoErros, Boolean inicializarDadosArquivo, ProgressBarVO progressBarVO, ConfiguracaoGeralSistemaVO  configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception {
			List<CidTipoRequerimentoVO> listaCidVOs = new ArrayList<CidTipoRequerimentoVO>(0); 
			if (inicializarDadosArquivo) {
				inicializarDadosArquivoImportarCid(cidTipoRequerimentoVO, uploadEvent, configuracaoGeralSistemaVO, usuario);
			}
			
			File arquivo = arquivo = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + PastaBaseArquivoEnum.ARQUIVO_IMPORTACAO_CID_TMP.getValue()
					+ File.separator + cidTipoRequerimentoVO.getArquivoVO().getNome());
			
			InputStream stream = new FileInputStream(arquivo);
			
			String extensao = uploadEvent.getUploadedFile().getName().substring(uploadEvent.getUploadedFile().getName().lastIndexOf(".") + 1);
			int rowMax = 0;
			XSSFSheet mySheetXlsx = null;
			HSSFSheet mySheetXls = null;
			if (extensao.equals("xlsx")) {
//				PARA XLSX UTILIZA XSSFWorkbook
				XSSFWorkbook workbook = new XSSFWorkbook(stream);
				mySheetXlsx = workbook.getSheetAt(0);
				rowMax = mySheetXlsx.getLastRowNum();

			} else {
//				PARA XLS UTILIZA HSSFWorkbook
				HSSFWorkbook workbook = new HSSFWorkbook(stream);
				mySheetXls = workbook.getSheetAt(0);
				rowMax = mySheetXls.getLastRowNum();
			}
			progressBarVO.setMaxValue(rowMax+1);
			int qtdeLinhaEmBranco = 0;
			int linha = 0;
			
			
			Row row = null;
			while (linha <= rowMax) {
				System.out.println("Linha : " +linha);
				progressBarVO.setStatus("Carregando informações da linha "+linha+" de "+rowMax);
				if (extensao.equals("xlsx")) {
					row = mySheetXlsx.getRow(linha);
				} else {
					row = mySheetXls.getRow(linha);
				}
				
				if (linha == 0) {
//					validarDadosCabecalhoExcelTabela(row);
					linha++;
					continue;
				}
				if (qtdeLinhaEmBranco == 1) {
					break;
				}
				if (getValorCelula(1, row, true) == null || getValorCelula(1, row, true).toString().equals("")) {
					qtdeLinhaEmBranco++;
					continue;
				}
//				System.out.println("Processando linha "+linha);
//				if (offset <= 0 ? numeroPessoa >= offset && numeroPessoa <= limit : numeroPessoa > offset && numeroPessoa <= offset + limit) {
					CidTipoRequerimentoVO cid = new CidTipoRequerimentoVO();
					
					if (progressBarVO.getForcarEncerramento()) {
						break;
					}
					int coluna = 0;
					try {
//					DADOS CANDIDATO
					cid.setCodCid(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : "");
					coluna++;
					cid.setDescricaoCid(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // COLUNA A
					coluna++;
//					candidatoInscricaoVO.setOrgaoEmissor(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // COLUNA B
//					coluna++;
//					candidatoInscricaoVO.setEstadoEmissaoRG(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // COLUNA C
//					coluna++;
					
					}catch (Exception e) {
						throw new Exception("Erro na linha "+(linha+1)+" e coluna "+ CellReference.convertNumToColString(coluna) +" o valor '"+row.getCell(coluna).getStringCellValue()+"' não condiz com o valor esperado.");
					}
					if(Uteis.isAtributoPreenchido(cid.getCodCid()) && Uteis.isAtributoPreenchido(cid.getDescricaoCid())){
						listaCidVOs.add(cid);
						linha++;
					} else {
						linha++;
					}
					
					
					progressBarVO.incrementar();		
					
			}
			
			return listaCidVOs;
		}
	 
	 
	 public void validarDadosCabecalhoExcelTabela(Row row) throws Exception {
			int index = 0;
			if (row.getCell(index) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("código cid")) {
				throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo CID, favor informe um Código CID.");
			}
			if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("descrição cid")) {
				throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo Descrição CID, favor informe uma descrição.");
			}
			
		}
}
