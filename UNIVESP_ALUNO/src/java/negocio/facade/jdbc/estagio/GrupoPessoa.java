package negocio.facade.jdbc.estagio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.blackboard.SalaAulaBlackboardRelatorioExcelVO;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardEnum;
import negocio.comuns.estagio.GrupoPessoaItemVO;
import negocio.comuns.estagio.GrupoPessoaVO;
//import negocio.comuns.faturamento.nfe.NotaFiscalEntradaVO;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisExcel;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.estagio.GrupoPessoaInterfaceFacade;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

@Repository
@Scope("singleton")
@Lazy
public class GrupoPessoa extends ControleAcesso implements GrupoPessoaInterfaceFacade{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7662437477733713134L;
	
	private static String idEntidade = "GrupoPessoa";

	public static String getIdEntidade() {
		return GrupoPessoa.idEntidade;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void valiarDados(GrupoPessoaVO obj) {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getNome()), UteisJSF.internacionalizar("msg_GrupoPessoa_nome"));		
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(GrupoPessoaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) {
		try {
			valiarDados(obj);
			if (obj.getCodigo() == 0) {
				incluir(obj, verificarAcesso, usuarioVO);
			} else {
				alterar(obj, verificarAcesso, usuarioVO);
			}			
			validarSeRegistroForamExcluidoDasListaSubordinadas(obj.getListaGrupoPessoaItemVO(), "grupoPessoaItem", idEntidade, obj.getCodigo(), usuarioVO);
			getFacadeFactory().getGrupoPessoaItemFacade().persistir(obj.getListaGrupoPessoaItemVO(), verificarAcesso, usuarioVO);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final GrupoPessoaVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			incluir(getIdEntidade(), verificarAcesso, usuario);
			incluir(obj, "grupoPessoa", new AtributoPersistencia()
					.add("nome", obj.getNome())					
					.add("pessoaSupervisorGrupo", obj.getPessoaSupervisorGrupo())					
					, usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final GrupoPessoaVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			alterar(getIdEntidade(), verificarAcesso, usuario);
			alterar(obj, "grupoPessoa", new AtributoPersistencia()
					.add("nome", obj.getNome())		
					.add("pessoaSupervisorGrupo", obj.getPessoaSupervisorGrupo())
					, new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(GrupoPessoaVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			excluir(getIdEntidade(), verificarAcesso, usuario);
			getConexao().getJdbcTemplate().update("DELETE FROM grupoPessoa WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario), obj.getCodigo());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void adicionarGrupoPessoaItemVO(GrupoPessoaVO obj,  GrupoPessoaItemVO gpi, UsuarioVO usuario) {
		Uteis.checkState(!Uteis.isAtributoPreenchido(gpi.getPessoaVO().getCodigo()), "O campo Avaliador/Facilitador deve ser informado.");
		Uteis.checkState(Uteis.isAtributoPreenchido(obj.getPessoaSupervisorGrupo().getCodigo()) && obj.getPessoaSupervisorGrupo().getCodigo().equals(gpi.getPessoaVO().getCodigo()), "O Supervisor não pode ser adicionado como Avaliador/Facilitador.");
		gpi.setGrupoPessoaVO(obj);
		gpi.getPessoaVO().setListaPessoaEmailInstitucionalVO(getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarListaPessoaEmailInstitucionalPorPessoa(gpi.getPessoaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		if(obj.getListaGrupoPessoaItemVO().stream().noneMatch(p-> p.getPessoaVO().getCodigo().equals(gpi.getPessoaVO().getCodigo()))) {
			obj.getListaGrupoPessoaItemVO().add(gpi);	
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void removerGrupoPessoaItemVO(GrupoPessoaVO obj,  GrupoPessoaItemVO gpi, UsuarioVO usuario) {		
		obj.getListaGrupoPessoaItemVO().removeIf(p-> p.getPessoaVO().getCodigo().equals(gpi.getPessoaVO().getCodigo()));
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void realizarImportacaoGrupoPessoa( List<GrupoPessoaVO> listaGrupoPessoa, UsuarioVO usuario) throws Exception {
		for (GrupoPessoaVO grupoPessoaVO : listaGrupoPessoa) {
			grupoPessoaVO.getListaGrupoPessoaItemVO().forEach(p -> {
					if(!p.getNovoObj() && !p.getPessoaVO().getSelecionado()) {
						p.setStatusAtivoInativoEnum(StatusAtivoInativoEnum.INATIVO);
						p.getPessoaVO().setSelecionado(true);
					}else if(p.getPessoaVO().getSelecionado()) {
						p.setStatusAtivoInativoEnum(StatusAtivoInativoEnum.ATIVO);
					}
				});
			
			grupoPessoaVO.getListaGrupoPessoaItemVO().removeIf(p-> !p.getPessoaVO().getSelecionado());
			persistir(grupoPessoaVO, true, usuario);
		}
	}
	
	@Override
	public File realizarExportacaoLayouGrupoPessoa(GrupoPessoaVO obj,  UsuarioVO usuario) throws Exception {
		File arquivo;
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Avaliadores-Facilitadores");
		UteisExcel uteisExcel = new UteisExcel(workbook);
		montarCabecalhoRelatorioExcel(uteisExcel, sheet);
		if(Uteis.isAtributoPreenchido(obj)) {
			montarItensRelatorioExcel(uteisExcel, sheet, obj);	
		}
		arquivo = new File(getCaminhoPastaWeb() + File.separator + "relatorio" + File.separator +"Layout Avaliadores-Facilitadores"+ new Date().getTime() + ".xlsx");
		FileOutputStream out = new FileOutputStream(arquivo);
		workbook.write(out);
		out.close();
		return arquivo;
	}
	
	private void montarCabecalhoRelatorioExcel(UteisExcel uteisExcel, XSSFSheet sheet) {
		int cellnum = 0;
		Row row;
		if (sheet.getLastRowNum() > 0) {
			row = sheet.createRow(sheet.getLastRowNum() + 1);
		} else {
			row = sheet.createRow(0);
		}
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 5000 , "Código");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 5000 , "Turma");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 10000 , "Componente Currigular");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 10000 , "Facilitador");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 10000 , "Supervisor");
	}
	
	private void montarItensRelatorioExcel(UteisExcel uteisExcel, XSSFSheet sheet, GrupoPessoaVO obj) {
		obj.getListaGrupoPessoaItemVO().forEach(p -> {
			int cellnum = 0;
			Row row = sheet.createRow(sheet.getLastRowNum() + 1);
			uteisExcel.preencherCelula(row, cellnum++, "");
			uteisExcel.preencherCelula(row, cellnum++, "");
			uteisExcel.preencherCelula(row, cellnum++, obj.getNome());
			uteisExcel.preencherCelula(row, cellnum++, Uteis.isAtributoPreenchido(p.getPessoaVO().getListaPessoaEmailInstitucionalVO()) ? p.getPessoaVO().getListaPessoaEmailInstitucionalVO().get(0).getEmail():"");
			uteisExcel.preencherCelula(row, cellnum++, Uteis.isAtributoPreenchido(obj.getPessoaSupervisorGrupo().getListaPessoaEmailInstitucionalVO()) ? obj.getPessoaSupervisorGrupo().getListaPessoaEmailInstitucionalVO().get(0).getEmail():"");
						
		});
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void upLoadArquivoImportado(InputStream uploadEvent, List<GrupoPessoaVO> listaGrupoPessoa, UsuarioVO usuario) throws Exception {
		XSSFWorkbook workbook = new XSSFWorkbook(uploadEvent);
		XSSFSheet sheet = workbook.getSheetAt(0);
		realizarLeituraLinhasGrupoImportado(listaGrupoPessoa, sheet, usuario);
	}
	
	public void realizarLeituraLinhasGrupoImportado(List<GrupoPessoaVO> listaGrupoPessoa, XSSFSheet sheet, UsuarioVO usuario) throws Exception {
		Iterator<?> linhas = sheet.rowIterator();
        while (linhas.hasNext()) {
        	XSSFRow linha = (XSSFRow) linhas.next();
        	if(linha != null &&  linha.getRowNum() != 0) {
        		realizarLeituraColunaGrupoImportado(listaGrupoPessoa, linha, usuario);
        	}
        }
        listaGrupoPessoa.forEach(t -> Ordenacao.ordenarLista(t.getListaGrupoPessoaItemVO(), "ordenacaoImportacao"));
	}
	
	public void realizarLeituraColunaGrupoImportado(List<GrupoPessoaVO> listaGrupoPessoa, XSSFRow linha,UsuarioVO usuario ) throws Exception {
		try {
			GrupoPessoaVO gp = new GrupoPessoaVO();
			GrupoPessoaItemVO gpi = new GrupoPessoaItemVO();
			Iterator<?> celulas = linha.cellIterator();
			while (celulas.hasNext()) {
				XSSFCell celula = (XSSFCell) celulas.next();
				int z = celula.getColumnIndex();
				switch (z) {
					case 2:
						gp = realizarVerificacaoGrupoPessoaExistente(listaGrupoPessoa, celula.toString().trim(), usuario);
						break;
					case 3:
						gpi = realizarVerificacaoGrupoPessoaItemExistente(gp, "",celula.toString().trim(), usuario);
						break;
					case 4:
						if(!Uteis.isAtributoPreenchido(gp.getPessoaSupervisorGrupo().getNome())) {
							gp.setPessoaSupervisorGrupo(realizarVerificacaoPessoaSupervisorExistente(gp, "", celula.toString().trim(), usuario));	
						}
						break;
//					case 3:
//						if(!Uteis.verificaCPF(gpi.getPessoaVO().getCPF()) || gpi.getPessoaVO().getNome().equals("CPF Não Cadastrado!")) {
//							gpi.setOperacao("CPF_INVALIDO");
//						}else {
//							gpi.setOperacao(celula.toString());
//							if(gpi.isOperacaoInativar()) {
//								gpi.setStatusAtivoInativoEnum(StatusAtivoInativoEnum.INATIVO);
//							}
//						}
//						break;
				}			
			}
			if(Uteis.isAtributoPreenchido(gpi.getPessoaVO().getEmail())) {
				String emailGrupoPessoaItem = gpi.getPessoaVO().getEmail();
				if(gp.getListaGrupoPessoaItemVO().stream().noneMatch(p-> p.getPessoaVO().getEmail().equals(emailGrupoPessoaItem))) {
					gp.getListaGrupoPessoaItemVO().add(gpi);
				}
			} else if(Uteis.isAtributoPreenchido(gpi.getPessoaVO().getCPF())) {
				String cpfGrupoPessoaItem = gpi.getPessoaVO().getCPF();
				if(gp.getListaGrupoPessoaItemVO().stream().noneMatch(p-> p.getPessoaVO().getCPF().equals(cpfGrupoPessoaItem))) {
					gp.getListaGrupoPessoaItemVO().add(gpi);
				}
			}
			int index = 0;
			for (GrupoPessoaVO objExistente : listaGrupoPessoa) {
				if (objExistente.getNome().equals(gp.getNome())) {
					listaGrupoPessoa.set(index, gp);
					return;
				}
				index++;
			}
			if(Uteis.isAtributoPreenchido(gp.getNome())) {
				listaGrupoPessoa.add(gp);	
			}			
		} catch (Exception e) {
			throw e;
		}
		
		
	}
	
	public GrupoPessoaVO realizarVerificacaoGrupoPessoaExistente(List<GrupoPessoaVO> listaGrupoPessoa, String nomeGrupo, UsuarioVO usuario) {		
		Optional<GrupoPessoaVO> findFirst = listaGrupoPessoa.stream().filter(p-> p.getNome().equalsIgnoreCase(nomeGrupo)).findFirst();
		if(findFirst.isPresent()) {
			return findFirst.get();
		}
		GrupoPessoaVO obj =  consultarPorNome(nomeGrupo, false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		if(!Uteis.isAtributoPreenchido(obj)) {
			obj.setNome(nomeGrupo);
		}
		return obj;
	}
	
	public GrupoPessoaItemVO realizarVerificacaoGrupoPessoaItemExistente(GrupoPessoaVO gp , String nomeCpf, String email, UsuarioVO usuario) throws Exception {
		Optional<GrupoPessoaItemVO> findFirst = null;
		if(Uteis.isAtributoPreenchido(email)) {
			findFirst = gp.getListaGrupoPessoaItemVO().stream().filter(p-> p.getPessoaVO().getListaPessoaEmailInstitucionalVO().stream().anyMatch(pp-> pp.getEmail().equalsIgnoreCase(email))).findFirst();
		}else if(Uteis.isAtributoPreenchido(nomeCpf)) {
			findFirst = gp.getListaGrupoPessoaItemVO().stream().filter(p-> p.getPessoaVO().getCPF().equalsIgnoreCase(nomeCpf)).findFirst();
		}else {
			return new GrupoPessoaItemVO();
		}		
		if(findFirst != null  && findFirst.isPresent()) {
			findFirst.get().getPessoaVO().setSelecionado(true);
			return findFirst.get();
		}
		
		GrupoPessoaItemVO grupoPessoaItem = new GrupoPessoaItemVO();
		grupoPessoaItem.setGrupoPessoaVO(gp);
		PessoaVO pessoaVO = new PessoaVO();
		if(Uteis.isAtributoPreenchido(email)) {
			pessoaVO = getFacadeFactory().getPessoaFacade().consultarPorEmaiInstitucionallUnico(email, false,Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		}else if(Uteis.isAtributoPreenchido(nomeCpf)) {
			pessoaVO = getFacadeFactory().getPessoaFacade().consultarPorCPFUnico(nomeCpf,0, "",false,Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		}		
		if(Uteis.isAtributoPreenchido(pessoaVO)) {
			Integer codigoPessoa  = pessoaVO.getCodigo();
			findFirst = gp.getListaGrupoPessoaItemVO().stream().filter(p-> p.getPessoaVO().getCodigo().equals(codigoPessoa)).findFirst();
			if(findFirst != null  && findFirst.isPresent()) {
				findFirst.get().getPessoaVO().setSelecionado(true);		
				return findFirst.get();
			}
			pessoaVO.setListaPessoaEmailInstitucionalVO(getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarListaPessoaEmailInstitucionalPorPessoa(pessoaVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
			grupoPessoaItem.setPessoaVO(pessoaVO);
			grupoPessoaItem.getPessoaVO().setEmail(email);
			grupoPessoaItem.getPessoaVO().setSelecionado(true);
			grupoPessoaItem.setStatusAtivoInativoEnum(StatusAtivoInativoEnum.ATIVO);
			return grupoPessoaItem;
		}
		grupoPessoaItem.getPessoaVO().setNome("Pessoa Não Cadastrada!");
		grupoPessoaItem.getPessoaVO().setSelecionado(false);
		grupoPessoaItem.getPessoaVO().setCPF(nomeCpf);
		grupoPessoaItem.getPessoaVO().setEmail(email);
		return grupoPessoaItem;
	}
	
	public PessoaVO realizarVerificacaoPessoaSupervisorExistente(GrupoPessoaVO gp , String nomeCpf, String email, UsuarioVO usuario) throws Exception {
		if(Uteis.isAtributoPreenchido(email)) {
			return getFacadeFactory().getPessoaFacade().consultarPorEmaiInstitucionallUnico(email, false,Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		}else if(Uteis.isAtributoPreenchido(nomeCpf)) {
			return  getFacadeFactory().getPessoaFacade().consultarPorCPFUnico(nomeCpf,0, "",false,Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		}
		return null;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void consultar(DataModelo dataModelo, GrupoPessoaVO obj) throws Exception {
		dataModelo.getListaConsulta().clear();
		dataModelo.getListaFiltros().clear();
		dataModelo.setListaConsulta(consultaRapidaPorFiltros(obj, dataModelo));
		dataModelo.getListaFiltros().clear();
		dataModelo.setTotalRegistrosEncontrados(consultarTotalPorFiltros(obj, dataModelo));
	}

	private List<GrupoPessoaVO> consultaRapidaPorFiltros(GrupoPessoaVO obj, DataModelo dataModelo) {
		try {
			consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE 1=1 ");
			montarFiltrosParaConsulta(obj, dataModelo, sqlStr);
			sqlStr.append(" ORDER BY grupoPessoa.codigo desc ");
			UteisTexto.addLimitAndOffset(sqlStr, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return montarDadosConsulta(tabelaResultado, dataModelo.getNivelMontarDados(), dataModelo.getUsuario());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	private Integer consultarTotalPorFiltros(GrupoPessoaVO obj, DataModelo dataModelo) {
		try {
			StringBuilder sqlStr = new StringBuilder();
			sqlStr.append(" select count (t.codigo) as qtde from ( ");
			sqlStr.append(" select distinct  grupoPessoa.codigo ");
			sqlStr.append(" FROM grupoPessoa ");
			sqlStr.append(" left join grupoPessoaItem on  grupoPessoaItem.grupoPessoa = grupoPessoa.codigo ");
			sqlStr.append(" left join pessoa as pessoa_supervisor on  pessoa_supervisor.codigo = grupoPessoa.pessoaSupervisorGrupo ");
			sqlStr.append(" left join pessoa on  pessoa.codigo = grupoPessoaItem.pessoa ");
			sqlStr.append(" WHERE 1= 1 ");
			montarFiltrosParaConsulta(obj, dataModelo, sqlStr);
			sqlStr.append(" ) as t ");
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return (Integer) Uteis.getSqlRowSetTotalizador(rs, "qtde", TipoCampoEnum.INTEIRO);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}


	private void montarFiltrosParaConsulta(GrupoPessoaVO obj, DataModelo dataModelo, StringBuilder sqlStr) {
		if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
			sqlStr.append(" and grupoPessoa.codigo = ? ");
			dataModelo.getListaFiltros().add(obj.getCodigo());
		}
		if (Uteis.isAtributoPreenchido(obj.getNome())) {
			sqlStr.append(" and lower(sem_acentos(grupoPessoa.nome)) like(lower(sem_acentos(?)))");
			dataModelo.getListaFiltros().add(PERCENT + obj.getNome().toLowerCase() + PERCENT);
		}
		if (Uteis.isAtributoPreenchido(obj.getFiltroNomePessoa())) {
			sqlStr.append(" and lower(sem_acentos(pessoa.nome)) like(lower(sem_acentos(?)))");
			dataModelo.getListaFiltros().add(PERCENT + obj.getFiltroNomePessoa().toLowerCase() + PERCENT);
		}
		if (Uteis.isAtributoPreenchido(obj.getFiltroStatusAtivoInativoEnum())) {
			sqlStr.append(" and grupoPessoaItem.statusAtivoInativoEnum = ? ");
			dataModelo.getListaFiltros().add(obj.getFiltroStatusAtivoInativoEnum().name());
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<GrupoPessoaVO> consultaGrupoPessoaCombobox(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" order by grupoPessoa.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public GrupoPessoaVO consultarPorCurso(Integer curso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sql = new StringBuilder("select grupoPessoa.codigo,  grupoPessoa.nome ");		
			sql.append(" FROM grupoPessoa ");
			sql.append(" inner join curso on  curso.grupoPessoaAnaliseRelatorioFinalEstagio = grupoPessoa.codigo ");
			sql.append(" and curso.codigo = ? ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), curso);
			if (!tabelaResultado.next()) {
				return new GrupoPessoaVO();
			}
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public GrupoPessoaVO consultarPorNome(String nome, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" where lower(sem_acentos(grupoPessoa.nome)) = (lower(sem_acentos(?))) ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), nome);
			if (!tabelaResultado.next()) {
				return new GrupoPessoaVO();
			}
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public GrupoPessoaVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" where grupoPessoa.codigo = ? ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigoPrm);
			if (!tabelaResultado.next()) {
				throw new StreamSeiException("Dados Não Encontrados ( GrupoPessoaVO ).");
			}
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder("select distinct COUNT(*) OVER() as totalRegistroConsulta,");
		sql.append(" grupoPessoa.codigo, ");
		sql.append(" grupoPessoa.nome, ");		
		
		sql.append(" pessoa_supervisor.codigo as \"pessoa_supervisor.codigo\", ");
		sql.append(" pessoa_supervisor.nome as \"pessoa_supervisor.nome\", ");
		sql.append(" pessoa_supervisor.cpf as \"pessoa_supervisor.cpf\", ");
		sql.append(" pessoa_supervisor.email as \"pessoa_supervisor.email\" ");
		
		sql.append(" FROM grupoPessoa ");
		sql.append(" left join grupoPessoaItem on  grupoPessoaItem.grupoPessoa = grupoPessoa.codigo ");
		sql.append(" left join pessoa as pessoa_supervisor on  pessoa_supervisor.codigo = grupoPessoa.pessoaSupervisorGrupo ");
		sql.append(" left join pessoa on  pessoa.codigo = grupoPessoaItem.pessoa ");

		return sql;
	}
	

	private List<GrupoPessoaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<GrupoPessoaVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	private GrupoPessoaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		GrupoPessoaVO obj = new GrupoPessoaVO();
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setNome(dadosSQL.getString("nome"));
		obj.getPessoaSupervisorGrupo().setCodigo(dadosSQL.getInt("pessoa_supervisor.codigo"));
		obj.getPessoaSupervisorGrupo().setNome(dadosSQL.getString("pessoa_supervisor.nome"));
		obj.getPessoaSupervisorGrupo().setCPF(dadosSQL.getString("pessoa_supervisor.CPF"));
		obj.getPessoaSupervisorGrupo().setEmail(dadosSQL.getString("pessoa_supervisor.email"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.getPessoaSupervisorGrupo().setListaPessoaEmailInstitucionalVO(getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarListaPessoaEmailInstitucionalPorPessoa(obj.getPessoaSupervisorGrupo().getCodigo(), nivelMontarDados, usuario));
		obj.setListaGrupoPessoaItemVO(getFacadeFactory().getGrupoPessoaItemFacade().consultarPorGrupoPessoaVO(obj, null, nivelMontarDados, usuario));
		return obj;

	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<GrupoPessoaVO> consultarGrupoPessoaAtivoAgrupandoFacilitadorQtdeEstagiosParaRedistribuicaoFacilitadoresPorSituacaoEstagio(List<String> situacoesEstagio,  boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select grupopessoa.codigo as \"codigoGrupo\" , grupopessoa.nome as \"nomeGrupo\", grupopessoaitem.codigo as \"codigoGrupoItem\" , pessoa.codigo as \"codigoFacilitador\" ,  pessoa.nome as \"facilitador\" ,count(estagio.codigo) as qtdeEstagio,  ");
		sqlStr.append(" array_to_string(array_agg(case when estagio.codigo is not null and estagio.codigo != 0 then estagio.codigo end   order by estagio.codigo), ', ') as estagios  from grupopessoa ");
		sqlStr.append(" inner join grupopessoaitem on  grupopessoa.codigo = grupopessoaitem.grupopessoa and grupopessoaitem.statusativoinativoenum = 'ATIVO' ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = grupopessoaitem.pessoa  ");
		sqlStr.append(" left join estagio on estagio.grupopessoaitem = grupopessoaitem.codigo and situacaoestagioenum in (").append(UteisTexto.converteListaStringParaString(situacoesEstagio)).append(") ");
		sqlStr.append(" group by grupopessoa.nome, grupopessoa.codigo, pessoa.codigo, pessoa.nome ,grupopessoaitem.codigo ");
		sqlStr.append(" order by grupopessoa.nome,  pessoa.nome ");		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());	
		List<GrupoPessoaVO> grupoPessoaVOs = new ArrayList<GrupoPessoaVO>(0);				
		while (tabelaResultado.next()) {		
			    GrupoPessoaVO obj  = consultarGrupoPessoaVO(tabelaResultado.getInt("codigoGrupo"), grupoPessoaVOs);	       
				obj.setNovoObj(Boolean.FALSE);
				obj.setCodigo(tabelaResultado.getInt("codigoGrupo"));
				obj.setNome(tabelaResultado.getString("nomeGrupo"));			
				GrupoPessoaItemVO grupoItem = new GrupoPessoaItemVO();
				grupoItem.setGrupoPessoaVO(obj);
				grupoItem.setCodigo(tabelaResultado.getInt("codigoGrupoItem"));
				grupoItem.getPessoaVO().setCodigo(tabelaResultado.getInt("codigoFacilitador"));
				grupoItem.getPessoaVO().setNome(tabelaResultado.getString("facilitador"));	
				grupoItem.setQtdeEstagioEmCorrecaoAnalise(tabelaResultado.getInt("qtdeEstagio"));
				grupoItem.setCodigoEstagios(tabelaResultado.getString("estagios"));	
				obj.getListaGrupoPessoaItemVO().add(grupoItem);	
				addGrupoPessoaVO(obj, grupoPessoaVOs);
		}
		return grupoPessoaVOs;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarRedistribuicaoGrupoFacilitadores(GrupoPessoaVO objGrupoPessoaVO, boolean controlarAcesso,UsuarioVO usuarioLogado) {		
		 
		if(objGrupoPessoaVO.getListaGrupoPessoaItemVO().isEmpty()) {
			return ;
		}	
		
		List<Integer> listaCodigosEstagios = new ArrayList<Integer>(0);
		objGrupoPessoaVO.getListaGrupoPessoaItemVO().stream().forEach(gpVO ->  {		
			for(String codigoEstagio : gpVO.getCodigoEstagios().split(",")) {
				if(!codigoEstagio.isEmpty() && Uteis.getIsValorNumerico(codigoEstagio)) {
					listaCodigosEstagios.add(Integer.parseInt(codigoEstagio.trim()));				
				}
			}
		});
		
		 objGrupoPessoaVO.getListaGrupoPessoaItemVO().stream().forEach( gpi-> gpi.getListarCodigoEstagioRedistribuir().clear());
		
		 int contadorGrupoItem = 0;		
		for(Integer  codigoEstagioDistribuir : listaCodigosEstagios) {
		     objGrupoPessoaVO.getListaGrupoPessoaItemVO().get(contadorGrupoItem).getListarCodigoEstagioRedistribuir().add(codigoEstagioDistribuir);	
		     contadorGrupoItem++;
		 	 if(contadorGrupoItem >= objGrupoPessoaVO.getListaGrupoPessoaItemVO().size()) {	 		 
		 		contadorGrupoItem = 0;	 
		 	 }		
		}	
		 objGrupoPessoaVO.getListaGrupoPessoaItemVO().stream().forEach( gpi-> {			 
		    getFacadeFactory().getEstagioFacade().
		    realizarAtualizacaoGrupoPessoaItemEstagioPorRedistribuicaoFacilitadores(gpi.getCodigo(),gpi.getListarCodigoEstagioRedistribuir() , controlarAcesso, usuarioLogado);		 
		 });	
		
	}
	
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void addGrupoPessoaVO(GrupoPessoaVO objGrupo, List<GrupoPessoaVO> grupoPessoaVOs) throws Exception {
		int index = 0;
		for (GrupoPessoaVO objsExistente : grupoPessoaVOs) {
			if (objsExistente.getCodigo().equals(objGrupo.getCodigo())) {
				grupoPessoaVOs.set(index, objGrupo);
				return;
			}
			index++;
		}
		grupoPessoaVOs.add(objGrupo);
	}	
	private GrupoPessoaVO consultarGrupoPessoaVO(Integer codigoGrupo, List<GrupoPessoaVO> grupoPessoaVOs) throws Exception {
		return grupoPessoaVOs.stream().filter(p-> p.getCodigo().equals(codigoGrupo)).findFirst().orElse(new GrupoPessoaVO());
	}
}
