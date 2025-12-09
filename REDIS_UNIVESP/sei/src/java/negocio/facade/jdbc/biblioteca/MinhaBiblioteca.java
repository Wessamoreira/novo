package negocio.facade.jdbc.biblioteca;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.ConsiderarFeriadoEnum;
import negocio.comuns.biblioteca.BibliotecaVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.biblioteca.ConfiguracaoBibliotecaVO;
import negocio.comuns.biblioteca.TimeLineVO;
import negocio.comuns.biblioteca.enumeradores.TipoOrigemTimeLineEnum;
import negocio.comuns.utilitarias.TipoFiltroConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.dominios.TipoUsuario;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.biblioteca.MinhaBibliotecaInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class MinhaBiblioteca extends ControleAcesso implements MinhaBibliotecaInterfaceFacade{
	
	 protected static String idEntidade;
	
	public MinhaBiblioteca() throws Exception {
        super();
        setIdEntidade("MinhaBiblioteca");
    }
	
	public static String getIdEntidade() {
		return idEntidade;
	}
	public static void setIdEntidade(String idEntidade) {
		MinhaBiblioteca.idEntidade = idEntidade;
	}
	public List<CatalogoVO> consultarPorTitulo(String urlAcessoExternoAplicacao ,  String titulo, boolean controlarAcesso, Integer biblioteca ,  UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT codigo,titulo,subtitulo,edicao,");
		sqlStr.append(" (select count(ex.codigo) from exemplar ex where ex.catalogo = catalogo.codigo and ex.situacaoatual = 'DI'  and (ex.paraconsulta = false or ex.paraConsulta is null)) as nrexemplaresdisponivel, ");
		sqlStr.append(" (select count(ex.codigo) from exemplar ex where ex.catalogo = catalogo.codigo and (ex.situacaoatual in ('DI','CO'))) as nrexemplaresemprestimo, ");
		sqlStr.append(" (select count(ex.codigo) from exemplar ex where ex.catalogo = catalogo.codigo and ex.situacaoatual = 'EM') as nrexemplaresemprestado, ");
		sqlStr.append(" (select count(ex.codigo) from exemplar ex where ex.catalogo = catalogo.codigo and (ex.situacaoatual in ('DI', 'CO') and ex.paraconsulta = true)) as nrexemplaresparaconsulta ");
		sqlStr.append(" FROM catalogo");
		if (!titulo.equals("")) {
			sqlStr.append(" where titulo ilike '%").append(titulo).append("%'");
		}
		sqlStr.append(" ORDER BY titulo limit 48");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        
        List<CatalogoVO> CatalogoVOs = new ArrayList<CatalogoVO>();
   
        while (tabelaResultado.next()) {
		        
        CatalogoVO obj = new CatalogoVO();
        obj.setCodigo(tabelaResultado.getInt("codigo"));
        obj.setTitulo(tabelaResultado.getString("titulo"));
        obj.setSubtitulo(tabelaResultado.getString("subtitulo"));
        obj.setEdicao(tabelaResultado.getString("edicao"));
        obj.setNrExemplaresDisponivel(tabelaResultado.getInt("nrexemplaresdisponivel"));
        obj.setNrExemplaresParaEmprestimo(tabelaResultado.getInt("nrexemplaresemprestimo"));
        obj.setNrExemplaresEmprestado(tabelaResultado.getInt("nrexemplaresemprestado"));
        obj.setNrExemplaresParaConsulta(tabelaResultado.getInt("nrexemplaresparaconsulta"));
        obj.setAutorVOs(getFacadeFactory().getAutorFacade().consultaRapidaNivelComboBoxPorCodigoCatalogo(obj.getCodigo(), usuario));
        obj.setNomeArquivoCapa(getFacadeFactory().getArquivoFacade().consultarCapaCatalogo(obj.getCodigo()));
        if (!obj.getNomeArquivoCapa().equals("")) {
        	if(new File(urlAcessoExternoAplicacao + "/arquivosBibliotecaExterna/"+ obj.getCodigo()+"/"+ obj.getNomeArquivoCapa()).canRead()) {
        		obj.setCaminhoImagemCapa(urlAcessoExternoAplicacao + "/arquivosBibliotecaExterna/"+ obj.getCodigo()+"/"+ obj.getNomeArquivoCapa());
        	}
		}
        CatalogoVOs.add(obj);
    }
        return CatalogoVOs;
	}
	
	public List<CatalogoVO> consultarCatalogos(String codigoCatalogo , DataModelo dataModelo, BibliotecaVO bibliotecaVO, List<TipoFiltroConsulta> tipoFiltroConsultas, String filtroTopo , String ordenarPor, String urlAcessoExternoAplicacao,String codigoTombo, String titulo, String assunto, String palavrasChave, String autores, String classificacao, String isbn, String issn, UsuarioVO usuario) throws Exception {
//		StringBuilder sqlQtde = new StringBuilder("select count(catalogo.codigo) as qtde from catalogo ");
		StringBuilder sqlStr = new StringBuilder("SELECT count(*) over() as qtde_total_registros, codigo, titulo, subtitulo, edicao, isbn,issn,cutterpha,classificacaobibliografica,");
		sqlStr.append(" (select count(ex.codigo) from exemplar ex where ex.catalogo = catalogo.codigo and ex.situacaoatual = 'DI'  and (ex.paraconsulta = false or ex.paraConsulta is null)) as nrexemplaresdisponivel, ");
		sqlStr.append(" (select count(ex.codigo) from exemplar ex where ex.catalogo = catalogo.codigo and (ex.situacaoatual in ('DI','CO'))) as nrexemplaresemprestimo, ");
		sqlStr.append(" (select count(ex.codigo) from exemplar ex where ex.catalogo = catalogo.codigo and ex.situacaoatual = 'EM') as nrexemplaresemprestado, ");
		sqlStr.append(" (select count(ex.codigo) from exemplar ex where ex.catalogo = catalogo.codigo and (ex.situacaoatual in ('DI', 'CO') and ex.paraconsulta = true)) as nrexemplaresparaconsulta ");
		sqlStr.append(" from catalogo ");
		sqlStr.append(getCondicaoWhere(codigoCatalogo , bibliotecaVO, tipoFiltroConsultas, filtroTopo ,  titulo, assunto,palavrasChave,  autores,classificacao, isbn,issn));
//		sqlQtde.append(getCondicaoWhere(codigoCatalogo  ,bibliotecaVO, tipoFiltroConsultas, filtroTopo ,  titulo, assunto,palavrasChave,  autores,classificacao, isbn,issn, editora));
		 if(Uteis.isAtributoPreenchido(ordenarPor)) {
			 sqlStr.append(" order by ").append(ordenarPor); 
		 }else {
			 sqlStr.append(" order by catalogo.titulo ");
		 }		
		 sqlStr.append(" limit ").append(dataModelo.getLimitePorPagina()).append(" offset ").append(dataModelo.getOffset()); 
        
        SqlRowSet tabelaResultado = null;
        if(Uteis.isAtributoPreenchido(filtroTopo)) {
        	tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), "%"+filtroTopo+"%", "%"+filtroTopo+"%");
        }else {
        	tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        }
        List<CatalogoVO> CatalogoVOs = new ArrayList<CatalogoVO>();
        if(tabelaResultado.next()) {
			dataModelo.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
			tabelaResultado.beforeFirst();
		
		while (tabelaResultado.next()) {

			CatalogoVO obj = new CatalogoVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setTitulo(tabelaResultado.getString("titulo"));
			obj.setSubtitulo(tabelaResultado.getString("subtitulo"));
			obj.setEdicao(tabelaResultado.getString("edicao"));
			obj.setAssunto(tabelaResultado.getString("assunto"));
			obj.setIsbn(tabelaResultado.getString("isbn"));
			obj.setIssn(tabelaResultado.getString("issn"));
			obj.setCutterPha(tabelaResultado.getString("cutterpha"));
			obj.setClassificacaoBibliografica(tabelaResultado.getString("classificacaobibliografica"));
			obj.setNrExemplaresDisponivel(tabelaResultado.getInt("nrexemplaresdisponivel"));
			obj.setNrExemplaresParaEmprestimo(tabelaResultado.getInt("nrexemplaresemprestimo"));
			obj.setNrExemplaresEmprestado(tabelaResultado.getInt("nrexemplaresemprestado"));
			obj.setNrExemplaresParaConsulta(tabelaResultado.getInt("nrexemplaresparaconsulta"));
			obj.setAutorVOs(getFacadeFactory().getAutorFacade()
					.consultaRapidaNivelComboBoxPorCodigoCatalogo(obj.getCodigo(), usuario));
			obj.setNomeArquivoCapa(getFacadeFactory().getArquivoFacade().consultarCapaCatalogo(obj.getCodigo()));
			if (!obj.getNomeArquivoCapa().equals("")) {
				if(new File(getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, usuario).getLocalUploadArquivoFixo() + File.separator + "arquivosBibliotecaExterna"+ File.separator + obj.getCodigo()+ File.separator + obj.getNomeArquivoCapa()).canRead()) {
					obj.setCaminhoImagemCapa(urlAcessoExternoAplicacao + "/arquivosBibliotecaExterna/"+obj.getCodigo()+"/"+ obj.getNomeArquivoCapa());
				}
			}
			CatalogoVOs.add(obj);

		}
        }else {
			dataModelo.setTotalRegistrosEncontrados(0);
		}
        dataModelo.setListaConsulta(CatalogoVOs);
//		if(Uteis.isAtributoPreenchido(filtroTopo)) {
//			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlQtde.toString(), "%"+filtroTopo+"%", "%"+filtroTopo+"%");
//		}else {
//			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlQtde.toString());
//		}
		
		return CatalogoVOs;
	}
	
	private StringBuilder getCondicaoWhere(String codigoCatalogo ,BibliotecaVO bibliotecaVO, List<TipoFiltroConsulta> tipoFiltroConsultas, String filtroTopo , String titulo, String assunto, String palavrasChave, String autores, String classificacao, String isbn, String issn) {
		StringBuilder sqlStr = new StringBuilder(" where 1 = 1 ");
		
		if(Uteis.isAtributoPreenchido(codigoCatalogo)) {
			sqlStr.append("and  catalogo.codigo  =  ").append(codigoCatalogo);		
		}
		if(Uteis.isAtributoPreenchido(filtroTopo)) {
			sqlStr.append(" and (sem_acentos(catalogo.titulo) ilike sem_acentos(?) ");			
			sqlStr.append(" or exists(select autor.codigo from catalogoautor inner join autor on catalogoautor.autor = autor.codigo where catalogoautor.catalogo = catalogo.codigo and  sem_acentos(autor.nome) ilike sem_acentos(?))) ");
		}
		if (Uteis.isAtributoPreenchido(titulo)) {
			sqlStr.append(" and (sem_acentos(catalogo.titulo) ilike sem_acentos('%").append(titulo).append("%'))");
		}
		if (Uteis.isAtributoPreenchido(assunto)) {
			sqlStr.append(" and (sem_acentos(catalogo.assunto) ilike sem_acentos('%").append(assunto).append("%'))");
		}
		if (Uteis.isAtributoPreenchido(palavrasChave)) {
			sqlStr.append(" and (sem_acentos(catalogo.palavrasChave) ilike sem_acentos('%").append(palavrasChave).append("%'))");
		}
		if (Uteis.isAtributoPreenchido(autores)) {
			sqlStr.append(" and (sem_acentos(catalogo.autores) ilike sem_acentos('%").append(autores).append("%'))");
		}
		if (Uteis.isAtributoPreenchido(classificacao)) {
			sqlStr.append(" and (sem_acentos(catalogo.classificacaobibliografica) ilike sem_acentos('%").append(classificacao).append("%'))");
		}
		if(Uteis.isAtributoPreenchido(bibliotecaVO)) {
			sqlStr.append(" and exists(select codigo from exemplar where catalogo.codigo = exemplar.catalogo and exemplar.biblioteca = ").append(bibliotecaVO.getCodigo()).append(" ) ");
		}
		if(Uteis.isAtributoPreenchido(tipoFiltroConsultas)) {
			StringBuilder inTipoCatalogo = new StringBuilder();
			StringBuilder inAreaConhecimento = new StringBuilder();
			StringBuilder inCurso = new StringBuilder();
			StringBuilder inDisciplina = new StringBuilder();
			for(TipoFiltroConsulta tipoFiltroConsulta: tipoFiltroConsultas) {
				switch (tipoFiltroConsulta.getCampoConsulta()) {
				case TIPO_CATALOGO:
					if(inTipoCatalogo.length() != 0) {
						inTipoCatalogo.append(", ");	
					}
					inTipoCatalogo.append(tipoFiltroConsulta.getKeyConsulta());
					break;
				case AREA_CONHECIMENTO:
					if(inAreaConhecimento.length() != 0) {
						inAreaConhecimento.append(", ");	
					}
					inAreaConhecimento.append(tipoFiltroConsulta.getKeyConsulta());
					break;
				case CURSO:
					if(inCurso.length() != 0) {
						inCurso.append(", ");	
					}
					inCurso.append(tipoFiltroConsulta.getKeyConsulta());
					break;	
				case DISCIPLINA:
					if(inDisciplina.length() != 0) {
						inDisciplina.append(", ");	
					}
					inDisciplina.append(tipoFiltroConsulta.getKeyConsulta());
					break;		

				default:
					break;
				}
			}
			if(Uteis.isAtributoPreenchido(inAreaConhecimento)) {
				
				sqlStr.append(" and exists(select catalogoareaconhecimento.codigo from catalogoareaconhecimento where catalogoareaconhecimento.catalogo = catalogo.codigo ");
				sqlStr.append(" and catalogoareaconhecimento.areaconhecimento in (").append(inAreaConhecimento).append(") limit 1) ");
			}
			if(Uteis.isAtributoPreenchido(inCurso)) {
				
				sqlStr.append(" and exists(select catalogocurso.codigo from catalogocurso where catalogocurso.catalogo = catalogo.codigo ");
				sqlStr.append(" and catalogocurso.curso in (").append(inCurso).append(") limit 1) ");
			}
			if(Uteis.isAtributoPreenchido(inTipoCatalogo)) {
				sqlStr.append(" and catalogo.tipocatalogo in (").append(inTipoCatalogo).append(") ");
			}
			if(Uteis.isAtributoPreenchido(inDisciplina)) {
				sqlStr.append(" and exists( select referenciabibliografica.catalogo  from referenciabibliografica  where referenciabibliografica.catalogo = catalogo.codigo and  disciplina = ").append(inDisciplina).append(")");
			}
			
		}
		return sqlStr;
	}

	
	public List <BibliotecaVO> consultarBiblioteca(){		
			StringBuilder sqlStr = new StringBuilder("SELECT codigo,nome ,configuracaoBiblioteca   FROM biblioteca ");
				
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		
		List<BibliotecaVO> bibliotecaVO = new ArrayList<BibliotecaVO>();
		
		while (tabelaResultado.next()) {
			
			BibliotecaVO obj = new BibliotecaVO();
			
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setNome(tabelaResultado.getString("nome"));
			obj.getConfiguracaoBiblioteca().setCodigo(tabelaResultado.getInt("configuracaoBiblioteca"));
			
			bibliotecaVO.add(obj);
			
		}
		
		return bibliotecaVO;
		
	}
	
	public List<TimeLineVO> listarDadosTimeLine(Integer codigoPessoa , UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT  bibli.nome as biblioteca,exemp.catalogo as codigoCatalogo ,cata.titulo  AS catalogo,exemp.codigobarra AS exemplar,itememp.situacao  AS situacaoemprestimo,emp.data AS dataemprestimo,itememp.datadevolucao,itememp.dataprevisaodevolucao AS dataprevdevolucao,");
		sqlStr.append(" itememp.valormulta AS valormulta,emp.valortotalmulta AS multatotal,((contareceber.codigo IS NULL OR contareceber.situacao = 'RE')");
		sqlStr.append(" AND itememp.situacao = 'DE') AS multapaga,tipocatalogo.nome AS tipocatalogo,itememp.isentarcobrancamulta,itememp.tipoemprestimo AS tipoitememprestimo,");
		sqlStr.append(" itememp.valorisencao,itememp.motivoisencao AS motivoisencao,");
		sqlStr.append(" CASE WHEN itememp.situacao = 'DE' THEN datadevolucao::date - dataprevisaodevolucao::date ELSE ");
		sqlStr.append(" CASE WHEN itememp.situacao = 'EX' THEN");
		sqlStr.append(" CASE WHEN (dataprevisaodevolucao >= CURRENT_DATE) THEN datadevolucao::date - dataprevisaodevolucao::date ELSE CURRENT_DATE::date - dataprevisaodevolucao::date END ELSE");
		sqlStr.append(" CASE WHEN itememp.situacao = 'ECA' THEN CURRENT_DATE::date - dataprevisaodevolucao::date ELSE");
		sqlStr.append(" CASE WHEN itememp.situacao = 'ESA' THEN CURRENT_DATE::date - dataprevisaodevolucao::date ELSE");
		sqlStr.append(" CASE WHEN itememp.situacao = 'DCA' THEN datadevolucao::date - dataprevisaodevolucao::date ELSE");
		sqlStr.append(" CASE WHEN itememp.situacao = 'DCA' THEN datadevolucao::date - dataprevisaodevolucao::date END");
		sqlStr.append(" END  END END END END diasatraso ,  exemp.emprestarSomenteFinalDeSemana , emp.tipoPessoa , bibli.cidade , bibli.configuracaoBiblioteca , bibli.codigo as codigoBiblioteca , contareceber.dataVencimento as dataVencimentoContaReceber");
		sqlStr.append(" FROM emprestimo emp");
		sqlStr.append(" INNER JOIN  itememprestimo itememp ON emp.codigo = itememp.emprestimo");
		sqlStr.append(" INNER JOIN  exemplar exemp ON exemp.codigo = itememp.exemplar");
		sqlStr.append(" INNER JOIN  catalogo cata  ON exemp.catalogo = cata.codigo");
		sqlStr.append(" INNER JOIN  biblioteca bibli ON emp.biblioteca = bibli.codigo");
		sqlStr.append(" LEFT JOIN   unidadeensinobiblioteca ueb	ON ueb.biblioteca = bibli.codigo AND ueb.unidadeensino = emp.unidadeensino");
		sqlStr.append(" LEFT JOIN   unidadeensino une ON une.codigo = ueb.unidadeensino	AND emp.unidadeensino = une.codigo ");
		sqlStr.append(" INNER JOIN  pessoa p ON p.codigo = emp.pessoa");
		sqlStr.append(" LEFT JOIN   usuario usr	ON emp.atendente = usr.codigo");
		sqlStr.append(" LEFT JOIN   usuario usrdelv ON itememp.responsaveldevolucao = usrdelv.codigo");
		sqlStr.append(" LEFT JOIN   matricula mat ON  mat.matricula = emp.matricula");
		sqlStr.append(" LEFT JOIN   curso cur ON cur.codigo = mat.curso");
		sqlStr.append(" LEFT JOIN   contareceber ON contareceber.codigo = itememp.contareceber");
		sqlStr.append(" LEFT JOIN   tipocatalogo ON tipocatalogo = tipocatalogo.codigo");
		sqlStr.append(" WHERE  p.codigo =").append(codigoPessoa);
		sqlStr.append(" GROUP BY bibli.nome ,exemp.catalogo , cata.codigo,cata.titulo,exemp.codigobarra,contareceber.codigo,contareceber.situacao,emp.data,itememp.situacao,itememp.datadevolucao,itememp.dataprevisaodevolucao,");
		sqlStr.append(" itememp.valormulta,emp.valortotalmulta,tipocatalogo.nome,itememp.isentarcobrancamulta,itememp.tipoemprestimo,itememp.valorisencao,itememp.motivoisencao,usrdelv.nome ,  exemp.emprestarSomenteFinalDeSemana , emp.tipoPessoa , bibli.cidade ,bibli.configuracaoBiblioteca , bibli.codigo , dataVencimentoContaReceber");
		sqlStr.append(" ORDER BY emp.data, cata.titulo");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado , false , usuarioVO);
		
	}
	
	private List montarDadosConsulta(SqlRowSet dadosSQL , Boolean reserva , UsuarioVO usuarioVO) throws Exception {
		ArrayList lista = new ArrayList(0);
		while (dadosSQL.next()) {
			lista.add(montarDados(dadosSQL , reserva , usuarioVO));
		}
		return lista;
	}
	
	private TimeLineVO montarDados(SqlRowSet dados , Boolean reserva , UsuarioVO usuarioVO) throws Exception {
		
		TimeLineVO vo = new TimeLineVO();
		
		vo.setBiblioteca(dados.getString("biblioteca"));
		vo.setCodigoCatalogo(dados.getInt("codigoCatalogo"));
		vo.setCatalogo(dados.getString("catalogo"));
		vo.setReserva(reserva);
		if (reserva == true) {
			vo.setDataReserva(Uteis.getData(dados.getDate("dataReserva") ,"dd/MM/yyyy hh:mm"));
			vo.setDataTerminoReserva(Uteis.getData(dados.getDate("dataTerminoReserva"),"dd/MM/yyyy hh:mm"));
			vo.setSituacaoReserva(dados.getString("situacaoReserva"));
			return vo;
		}
		
				
		vo.setExemplar(dados.getString("exemplar"));
		if (dados.getString("tipoItemEmprestimo") != null && dados.getString("tipoItemEmprestimo").equals("HR")) {
			vo.setDataEmprestimo(Uteis.getData(dados.getDate("dataEmprestimo"), "dd/MM/yyyy hh:mm"));
			vo.setDataDevolucao(Uteis.getData(dados.getTimestamp("dataDevolucao"), "dd/MM/yyyy hh:mm"));
			vo.setDataPrevDevolucao(Uteis.getData(dados.getDate("dataPrevDevolucao"), "dd/MM/yyyy hh:mm"));
		} else {
			vo.setDataEmprestimo(Uteis.getData(dados.getDate("dataEmprestimo"), "dd/MM/yyyy"));
			vo.setDataDevolucao(Uteis.getData(dados.getDate("dataDevolucao"), "dd/MM/yyyy"));
			vo.setDataPrevDevolucao(Uteis.getData(dados.getDate("dataPrevDevolucao"), "dd/MM/yyyy"));
		}
		vo.setValorMulta(dados.getDouble("valorMulta"));
		vo.setTotalMultaEmprestimo(dados.getDouble("multaTotal"));
		vo.setSituacaoEmprestimo(dados.getString("situacaoEmprestimo"));
		vo.setMultaPaga(dados.getBoolean("multaPaga"));
		vo.setMultaIsenta(dados.getBoolean("isentarcobrancamulta"));
		vo.setValorMultaIsenta(dados.getDouble("valorisencao"));
		vo.setTipoItemEmprestimo(dados.getString("tipoItemEmprestimo"));
		Integer diasAtraso = new Double(dados.getDouble("diasAtraso")).intValue();
		if (diasAtraso <= 0) {
			vo.setTipoEmprestimo("EM DIA");
			vo.setDiasAtraso(0);
		} else {
			vo.setTipoEmprestimo("ATRASADO");
			vo.setDiasAtraso(diasAtraso);
		}
		vo.setTipoCatalogo(dados.getString("tipoCatalogo"));
		vo.setEmprestarSomenteFinalDeSemana(dados.getBoolean("emprestarSomenteFinalDeSemana"));
		vo.setTipoPessoaEmprestimo(dados.getString("tipoPessoa"));
		vo.getCidadeBibliotecaVO().setCodigo(dados.getInt("cidade"));
		vo.setCodigoBiblioteca(dados.getInt("codigoBiblioteca"));
		vo.setConfiguracaoBibliotecaVO(getFacadeFactory().getConfiguracaoBibliotecaFacade().consultarConfiguracaoPorBiblioteca(vo.getCodigoBiblioteca() , 1 , usuarioVO));
		vo.setDataVencimentoContaReceber(Uteis.getData(dados.getDate("dataVencimentoContaReceber") , "DD/MM/YYYY"));

		return vo;
	}
	
	public List<TimeLineVO> listarDadosTimeLineReservas(Integer codigoPessoa , UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder("select biblioteca.nome as biblioteca , reserva.catalogo as codigoCatalogo , catalogo.titulo as catalogo ,");
		sqlStr.append(" dataReserva , dataTerminoReserva ,situacao as situacaoReserva from reserva");
		sqlStr.append(" inner join catalogo on reserva.catalogo = catalogo.codigo");
		sqlStr.append(" inner join biblioteca on reserva.biblioteca = biblioteca.codigo");
		sqlStr.append(" WHERE  reserva.pessoa = ").append(codigoPessoa);
		
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado , true , usuarioVO);
		
	}
	
	public Double realizarCalculoMultaParcial(TimeLineVO timeLineVO, TipoPessoa tipoPessoa, ConfiguracaoBibliotecaVO confBibVO, CidadeVO cidadeVO) throws Exception {
		if (timeLineVO.getTipoEmprestimo().equals("HR")) {
			//realizarCalculoMultaDevolucaoItemEmprestimoHora(timeLineVO, tipoPessoa, confBibVO, cidadeVO, usuarioVO);
			return null;
		} else {
		return	realizarCalculoMultaDevolucaoItemEmprestimoDia(timeLineVO, tipoPessoa, confBibVO, cidadeVO);
		}
		
	}
	
	public Double realizarCalculoMultaDevolucaoItemEmprestimoDia(TimeLineVO timeLineVO, TipoPessoa tipoPessoa, ConfiguracaoBibliotecaVO confBibVO, CidadeVO cidadeVO) throws Exception {
		Double valorBase = 0.0;
		Date dataBaseCalculoMulta = new Date();
		Double valorMulta = 0.0;
		
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		Date dataPrevDevolucao = formato.parse(timeLineVO.getDataPrevDevolucao());
		
		if (tipoPessoa.equals(TipoPessoa.ALUNO)) {
			if (timeLineVO.getEmprestarSomenteFinalDeSemana()) {
				valorBase = confBibVO.getValorMultaEmprestimoAlunoFinalDeSemana();
			} else {
				valorBase = confBibVO.getValorMultaDiaAluno();
			}
		}
		if (tipoPessoa.equals(TipoPessoa.MEMBRO_COMUNIDADE)) {
			if (timeLineVO.getEmprestarSomenteFinalDeSemana()) {
				valorBase = confBibVO.getValorMultaEmprestimoVisitanteFinalDeSemana();
			} else {
				valorBase = confBibVO.getValorMultaDiaVisitante();
			}
		}
		if (tipoPessoa.equals(TipoPessoa.FUNCIONARIO)) {
			if (timeLineVO.getEmprestarSomenteFinalDeSemana()) {
				valorBase = confBibVO.getValorMultaEmprestimoProfessorFinalDeSemana();
			} else {
				valorBase = confBibVO.getValorMultaDiaFuncionario();
			}
		}
		if (tipoPessoa.equals(TipoPessoa.PROFESSOR)) {
			if (timeLineVO.getEmprestarSomenteFinalDeSemana()) {
				valorBase = confBibVO.getValorMultaEmprestimoFuncionarioFinalDeSemana();
			} else {
				valorBase = confBibVO.getValorMultaDiaProfessor();
			}
		}
		
		if (confBibVO.getUtilizarDiasUteisCalcularMulta()) {
		 return	valorMulta = valorBase * getFacadeFactory().getFeriadoFacade().calcularNrDiasUteis(dataPrevDevolucao, dataBaseCalculoMulta, Uteis.isAtributoPreenchido(cidadeVO.getCodigo()) ? cidadeVO.getCodigo() : 0, confBibVO.getConsiderarSabadoDiaUtil(), confBibVO.getConsiderarDomingoDiaUtil(), ConsiderarFeriadoEnum.BIBLIOTECA);
		} else {
			return	valorMulta = valorBase * timeLineVO.getDiasAtraso();
			}
		}
	
		@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	    public void criarCadastroVisitante(PessoaVO pessoaVO,  ConfiguracaoGeralSistemaVO configuracaoGeralSistema , String usernameMembroComunidade , String senhaMembroComunidade ) throws Exception {
	        UsuarioVO usuarioIncluir = criarUsuario(pessoaVO, configuracaoGeralSistema ,  usernameMembroComunidade , senhaMembroComunidade);  
	        usuarioIncluir.getPessoa().setMembroComunidade(true);
	        getFacadeFactory().getPessoaFacade().incluir(usuarioIncluir.getPessoa(), false, null, configuracaoGeralSistema, true);
	        getFacadeFactory().getUsuarioFacade().incluir(usuarioIncluir, false, null);

	    }
		
		@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
		public UsuarioVO criarUsuario(PessoaVO pessoaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistema , String usernameMembroComunidade , String senhaMembroComunidade) throws Exception {
	        UsuarioVO obj = new UsuarioVO();
	        obj.setNome(pessoaVO.getNome());
	        obj.setPessoa(pessoaVO);
			obj.setSenha(senhaMembroComunidade);
	        obj.setUsername(usernameMembroComunidade);
	        obj.setTipoUsuario(TipoUsuario.VISITANTE.getValor());
	        obj.setPerfilAdministrador(false);
	        return obj;
	    }
		public List<TimeLineVO> montarDadosTimeLine(UsuarioVO usuario) throws Exception {
			
			StringBuilder sqlStr = new StringBuilder(" select 'EMPRESTIMO' as tipoOrigemTimeLine, emprestimo.data as dataocorrencia, 2 as ordem, emprestimo.codigo as codigoorigem, catalogo.titulo, exemplar.codigobarra, itememprestimo.valormulta as valor, 0.0 as juro, 0.0 as multa, 0.0 as acrescimo, 0.0 as desconto, 0.0 as valorrecebido , TIMESTAMP '2019-01-01 00:00:00' as dataterminoreserva,	itememprestimo.dataprevisaodevolucao, emprestimo.data as dataemprestimo, itememprestimo.datadevolucao, '' as datarecebimentocontareceber, '' as datavencimentocontareceber ");
			                           sqlStr.append(" from itememprestimo ");
			                           sqlStr.append(" inner join emprestimo on itememprestimo.emprestimo = emprestimo.codigo");
			                           sqlStr.append(" inner join exemplar on itememprestimo.exemplar = exemplar.codigo");
			                           sqlStr.append(" inner join catalogo on exemplar.catalogo = catalogo.codigo");
			                           sqlStr.append(" where emprestimo.pessoa = ").append(usuario.getPessoa().getCodigo());
			                           sqlStr.append(" and not exists (");
			                           sqlStr.append(" 	select ie.codigo from itememprestimo ie");
			                           sqlStr.append(" 	inner join emprestimo em on em.codigo = ie.emprestimo");
			                           sqlStr.append(" 	where ie.exemplar = itememprestimo.exemplar");
			                           sqlStr.append(" 	and ie.datadevolucao::date = emprestimo.data::date");
			                           sqlStr.append(" 	and em.pessoa = emprestimo.pessoa");
			                           sqlStr.append(" 	and em.codigo < emprestimo.codigo");
			                           sqlStr.append(" )");
			                           sqlStr.append(" union all");
			                           sqlStr.append(" ");
			                           sqlStr.append(" select 'EMPRESTIMO_DEVOLVIDO_ATRASADO' as tipoOrigemTimeLine, itememprestimo.datadevolucao as dataocorrencia, 2.5 as ordem, emprestimo.codigo as codigoorigem , catalogo.titulo, exemplar.codigobarra, itememprestimo.valormulta as valor, 0.0 as juro, 0.0 as multa, 0.0 as acrescimo, 0.0 as desconto, 0.0 as valorrecebido , TIMESTAMP '2019-01-01 00:00:00' as dataterminoreserva,	itememprestimo.dataprevisaodevolucao, emprestimo.data as dataemprestimo, itememprestimo.datadevolucao, '' as datarecebimentocontareceber, '' as datavencimentocontareceber");
			                           sqlStr.append(" from itememprestimo ");
			                           sqlStr.append(" inner join emprestimo on itememprestimo.emprestimo = emprestimo.codigo");
			                           sqlStr.append(" inner join exemplar on itememprestimo.exemplar = exemplar.codigo");
			                           sqlStr.append(" inner join catalogo on exemplar.catalogo = catalogo.codigo");
			                           sqlStr.append(" where emprestimo.pessoa =").append(usuario.getPessoa().getCodigo());
			                           sqlStr.append(" and (itememprestimo.situacao = 'DE' and itememprestimo.datadevolucao is not null and itememprestimo.datadevolucao::date > itememprestimo.dataprevisaodevolucao::date)");
			                           sqlStr.append(" and not exists (");
			                           sqlStr.append(" 	select ie.codigo from itememprestimo ie");
			                           sqlStr.append(" 	inner join emprestimo em on em.codigo = ie.emprestimo");
			                           sqlStr.append(" 	where ie.exemplar = itememprestimo.exemplar");
			                           sqlStr.append(" 	and em.data::date = itememprestimo.datadevolucao::date");
			                           sqlStr.append(" 	and em.pessoa = emprestimo.pessoa");
			                           sqlStr.append(" 	and em.codigo > emprestimo.codigo");
			                           sqlStr.append(" )");
			                           sqlStr.append(" union all");
			                           sqlStr.append(" ");	
			                           sqlStr.append(" ");
			                           sqlStr.append(" select 'EMPRESTIMO_DEVOLVIDO_EM_DIA' as tipoOrigemTimeLine, itememprestimo.datadevolucao as dataocorrencia,  2.2 as ordem, emprestimo.codigo as codigoorigem, catalogo.titulo, exemplar.codigobarra , 0.0 as valor, 0.0 as juro, 0.0 as multa, 0.0 as acrescimo, 0.0 as desconto, 0.0 as valorrecebido , TIMESTAMP '2019-01-01 00:00:00' as dataterminoreserva,	itememprestimo.dataprevisaodevolucao, emprestimo.data as dataemprestimo, itememprestimo.datadevolucao, '' as datarecebimentocontareceber, '' as datavencimentocontareceber ");
			                           sqlStr.append(" from itememprestimo ");
			                           sqlStr.append(" inner join emprestimo on itememprestimo.emprestimo = emprestimo.codigo");
			                           sqlStr.append(" inner join exemplar on itememprestimo.exemplar = exemplar.codigo");
			                           sqlStr.append(" inner join catalogo on exemplar.catalogo = catalogo.codigo");
			                           sqlStr.append(" where emprestimo.pessoa = ").append(usuario.getPessoa().getCodigo());
			                           sqlStr.append(" and (itememprestimo.situacao = 'DE' and itememprestimo.datadevolucao is not null and itememprestimo.datadevolucao::date <= itememprestimo.dataprevisaodevolucao::date)");
			                           sqlStr.append(" and not exists (");
			                           sqlStr.append(" 	select ie.codigo from itememprestimo ie");
			                           sqlStr.append(" 	inner join emprestimo em on em.codigo = ie.emprestimo");
			                           sqlStr.append(" 	where ie.exemplar = itememprestimo.exemplar");
			                           sqlStr.append(" 	and em.data::date = itememprestimo.datadevolucao::date");
			                           sqlStr.append(" 	and em.pessoa = emprestimo.pessoa");
			                           sqlStr.append(" 	and em.codigo > emprestimo.codigo");
			                           sqlStr.append(" )");	
			                           sqlStr.append(" ");
			                           sqlStr.append(" union all");
			                           sqlStr.append(" select 'EMPRESTIMO_RENOVADO' as tipoOrigemTimeLine, itememprestimo.datadevolucao as dataocorrencia,  2.1 as ordem, emprestimo.codigo as codigoorigem, catalogo.titulo, exemplar.codigobarra , 0.0 as valor, 0.0 as juro, 0.0 as multa, 0.0 as acrescimo, 0.0 as desconto, 0.0 as valorrecebido , TIMESTAMP '2019-01-01 00:00:00' as dataterminoreserva,	itememprestimo.dataprevisaodevolucao, emprestimo.data as dataemprestimo, itememprestimo.datadevolucao, '' as datarecebimentocontareceber, '' as datavencimentocontareceber");
			                           sqlStr.append(" from itememprestimo ");
			                           sqlStr.append(" inner join emprestimo on itememprestimo.emprestimo = emprestimo.codigo");
			                           sqlStr.append(" inner join exemplar on itememprestimo.exemplar = exemplar.codigo");
			                           sqlStr.append(" inner join catalogo on exemplar.catalogo = catalogo.codigo");
			                           sqlStr.append(" where emprestimo.pessoa = ").append(usuario.getPessoa().getCodigo());;
			                           sqlStr.append(" and (itememprestimo.situacao = 'DE' and itememprestimo.datadevolucao is not null)");
			                           sqlStr.append(" and exists (");
			                           sqlStr.append(" 	select ie.codigo from itememprestimo ie");
			                           sqlStr.append(" 	inner join emprestimo em on em.codigo = ie.emprestimo");
			                           sqlStr.append(" 	where ie.exemplar = itememprestimo.exemplar");
			                           sqlStr.append(" 	and em.data::date = itememprestimo.datadevolucao::date");
			                           sqlStr.append(" 	and em.pessoa = emprestimo.pessoa");
			                           sqlStr.append(" 	and em.codigo > emprestimo.codigo");	
			                           sqlStr.append(" )");
			                           sqlStr.append(" ");
			                           sqlStr.append(" union all");
			                           sqlStr.append(" select 'EMPRESTIMO_ATRASADO' as tipoOrigemTimeLine, itememprestimo.dataprevisaodevolucao as dataocorrencia,  2.3 as ordem, emprestimo.codigo as codigoorigem , catalogo.titulo, exemplar.codigobarra , 0.0 as valor, 0.0 as juro, 0.0 as multa, 0.0 as acrescimo, 0.0 as desconto, 0.0 as valorrecebido , TIMESTAMP '2019-01-01 00:00:00' as dataterminoreserva,	itememprestimo.dataprevisaodevolucao, emprestimo.data as dataemprestimo, itememprestimo.datadevolucao, '' as datarecebimentocontareceber, '' as datavencimentocontareceber");
			                           sqlStr.append(" from itememprestimo ");
			                           sqlStr.append(" inner join emprestimo on itememprestimo.emprestimo = emprestimo.codigo");
			                           sqlStr.append(" inner join exemplar on itememprestimo.exemplar = exemplar.codigo");
			                           sqlStr.append(" inner join catalogo on exemplar.catalogo = catalogo.codigo");
			                           sqlStr.append(" where emprestimo.pessoa = ").append(usuario.getPessoa().getCodigo());
			                           sqlStr.append(" and (itememprestimo.situacao = 'EX' and itememprestimo.datadevolucao is null and itememprestimo.dataprevisaodevolucao::DATE < current_date)");
			                           sqlStr.append(" union all");
			                           sqlStr.append(" select 'EMPRESTIMO_EM_DIA' as tipoOrigemTimeLine, itememprestimo.dataprevisaodevolucao as dataocorrencia,  2.4 as ordem, emprestimo.codigo as codigoorigem , catalogo.titulo, exemplar.codigobarra , 0.0 as valor, 0.0 as juro, 0.0 as multa, 0.0 as acrescimo, 0.0 as desconto, 0.0 as valorrecebido , TIMESTAMP '2019-01-01 00:00:00' as dataterminoreserva,	itememprestimo.dataprevisaodevolucao, emprestimo.data as dataemprestimo, itememprestimo.datadevolucao, '' as datarecebimentocontareceber, '' as datavencimentocontareceber");
			                           sqlStr.append(" from itememprestimo ");
			                           sqlStr.append(" inner join emprestimo on itememprestimo.emprestimo = emprestimo.codigo");
			                           sqlStr.append(" inner join exemplar on itememprestimo.exemplar = exemplar.codigo");
			                           sqlStr.append(" inner join catalogo on exemplar.catalogo = catalogo.codigo");	
			                           sqlStr.append(" where emprestimo.pessoa = ").append(usuario.getPessoa().getCodigo());
			                           sqlStr.append(" and (itememprestimo.situacao = 'EX' and itememprestimo.datadevolucao is null and itememprestimo.dataprevisaodevolucao::DATE >= current_date)");
			                           sqlStr.append(" union all");
			                           sqlStr.append(" select 'CONTA_RECEBER_VENCIMENTO' as tipoOrigemTimeLine, contareceber.datavencimento as dataocorrencia ,  4 as ordem, contareceber.codigo as codigoorigem, array_to_string(array_agg(distinct catalogo.titulo), ', ') as titulo,  ''  as codigobarra, contareceber.valor, contareceber.valorjurocalculado as juro, contareceber.valormultacalculado as multa, contareceber.acrescimo, contareceber.valordescontocalculado as desconto, contareceber.valorrecebercalculado as valorrecebido , TIMESTAMP '2019-01-01 00:00:00' as dataterminoreserva,	TIMESTAMP '2019-01-01 00:00:00' as dataprevisaodevolucao, TIMESTAMP '2019-01-01 00:00:00' as  dataemprestimo, TIMESTAMP '2019-01-01 00:00:00' as datadevolucao, to_char(negociacaorecebimento.data, 'dd/MM/yyyy') as datarecebimentocontareceber, to_char(contareceber.datavencimento, 'dd/MM/yyyy') as datavencimentocontareceber");
			                           sqlStr.append(" from contareceber ");
			                           sqlStr.append(" inner join itememprestimo on itememprestimo.contareceber = contareceber.codigo");
			                           sqlStr.append(" inner join exemplar on itememprestimo.exemplar = exemplar.codigo");
			                           sqlStr.append(" inner join catalogo on exemplar.catalogo = catalogo.codigo");
			                           sqlStr.append(" left join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo");
			                           sqlStr.append(" left join negociacaorecebimento on contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo");
			                           sqlStr.append(" where contareceber.pessoa = ").append(usuario.getPessoa().getCodigo()).append(" and contareceber.tipoorigem = 'BIB' group by contareceber.datavencimento, negociacaorecebimento.data, contareceber.codigo, contareceber.valor, contareceber.valorjurocalculado, contareceber.valormultacalculado, contareceber.acrescimo, contareceber.valordescontocalculado, contareceber.valorrecebercalculado ");
			                           sqlStr.append(" union all");
			                           sqlStr.append(" select distinct 'CONTA_RECEBER_PAGAMENTO' as tipoOrigemTimeLine, negociacaorecebimento.data as dataocorrencia, 5 as ordem, contareceber.codigo as codigoorigem, array_to_string(array_agg(distinct catalogo.titulo), ', ') as titulo, '' as codigobarra, contareceber.valor as valor, contareceber.juro as juro, contareceber.multa as multa, contareceber.acrescimo as acrescimo, contareceber.valordescontocalculado as desconto, contareceber.valorrecebido as valorrecebido , TIMESTAMP '2019-01-01 00:00:00' as dataterminoreserva,	TIMESTAMP '2019-01-01 00:00:00' as dataprevisaodevolucao,	TIMESTAMP '2019-01-01 00:00:00' as dataemprestimo,	TIMESTAMP '2019-01-01 00:00:00' as datadevolucao, to_char(negociacaorecebimento.data, 'dd/MM/yyyy') as datarecebimentocontareceber, to_char(contareceber.datavencimento, 'dd/MM/yyyy') as datavencimentocontareceber");
			                           sqlStr.append(" from contareceber ");
			                           sqlStr.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo");
			                           sqlStr.append(" inner join negociacaorecebimento on contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo");
			                           sqlStr.append(" inner join itememprestimo on itememprestimo.contareceber = contareceber.codigo");
			                           sqlStr.append(" inner join exemplar on itememprestimo.exemplar = exemplar.codigo");
			                           sqlStr.append(" inner join catalogo on exemplar.catalogo = catalogo.codigo");
			                           sqlStr.append(" where contareceber.pessoa = ").append(usuario.getPessoa().getCodigo()).append(" and contareceber.tipoorigem = 'BIB'");
			                           sqlStr.append(" and contareceber.situacao = 'RE' group by negociacaorecebimento.data, contareceber.codigo, contareceber.valor, contareceber.juro, contareceber.multa, contareceber.acrescimo, contareceber.valordescontocalculado, contareceber.valorrecebido ");
			                           sqlStr.append(" union all");
			                           sqlStr.append(" select distinct 'RESERVA_ATIVA' as tipoOrigemTimeLine, reserva.datareserva as dataocorrencia, 1 as ordem, reserva.codigo as codigoorigem, catalogo.titulo, '' codigobarra, 0.0 as valor, 0.0 as juro, 0.0 as multa, 0.0 as acrescimo, 0.0 as desconto, 0.0 as valorrecebido , reserva.dataterminoreserva,	TIMESTAMP '2019-01-01 00:00:00' as dataprevisaodevolucao,	TIMESTAMP '2019-01-01 00:00:00' as dataemprestimo,	TIMESTAMP '2019-01-01 00:00:00' as datadevolucao, '' as datarecebimentocontareceber, '' as datavencimentocontareceber");
			                           sqlStr.append(" from reserva ");
			                           sqlStr.append(" inner join catalogo on reserva.catalogo = catalogo.codigo");
			                           sqlStr.append(" where reserva.pessoa =  ").append(usuario.getPessoa().getCodigo());
			                           sqlStr.append(" and reserva.situacao = 'EX'");
			                           sqlStr.append(" union all");
			                           sqlStr.append(" select distinct 'RESERVA_CANCELADA' as tipoOrigemTimeLine, reserva.dataterminoreserva as dataocorrencia, 1.1 as ordem, reserva.codigo as codigoorigem, catalogo.titulo, '' codigobarra, 0.0 as valor, 0.0 as juro, 0.0 as multa, 0.0 as acrescimo, 0.0 as desconto, 0.0 as valorrecebido , reserva.dataterminoreserva,	TIMESTAMP '2019-01-01 00:00:00' as dataprevisaodevolucao,	TIMESTAMP '2019-01-01 00:00:00' as dataemprestimo,	TIMESTAMP '2019-01-01 00:00:00' as datadevolucao, '' as datarecebimentocontareceber, '' as datavencimentocontareceber");
			                           sqlStr.append(" from reserva ");
			                           sqlStr.append(" inner join catalogo on reserva.catalogo = catalogo.codigo");
			                           sqlStr.append(" where reserva.pessoa = ").append(usuario.getPessoa().getCodigo());
			                           sqlStr.append(" and reserva.situacao = 'CA'");
			                           sqlStr.append(" union all");	
			                           sqlStr.append(" select distinct 'RESERVA_FINALIZADA' as tipoOrigemTimeLine, reserva.dataterminoreserva as dataocorrencia, 1.2 as ordem, reserva.codigo as codigoorigem, catalogo.titulo, '' codigobarra, 0.0 as valor, 0.0 as juro, 0.0 as multa, 0.0 as acrescimo, 0.0 as desconto, 0.0 as valorrecebido , reserva.dataterminoreserva,	TIMESTAMP '2019-01-01 00:00:00' as dataprevisaodevolucao,	TIMESTAMP '2019-01-01 00:00:00' as dataemprestimo,	TIMESTAMP '2019-01-01 00:00:00' as datadevolucao, '' as datarecebimentocontareceber, '' as datavencimentocontareceber");
			                           sqlStr.append(" from reserva ");
			                           sqlStr.append(" inner join catalogo on reserva.catalogo = catalogo.codigo");
			                           sqlStr.append(" where reserva.pessoa = ").append(usuario.getPessoa().getCodigo());
			                           sqlStr.append(" and reserva.situacao = 'FI'");
			                           sqlStr.append(" ");
			                           sqlStr.append(" union all");
			                           sqlStr.append(" select distinct 'BLOQUEIO_BIBLIOTECA' as tipoOrigemTimeLine, bloqueiobiblioteca.data as dataocorrencia, 5 as ordem, bloqueiobiblioteca.codigo as codigoorigem, bloqueiobiblioteca.motivobloqueio as titulo, '' codigobarra, 0.0 as valor, 0.0 as juro, 0.0 as multa, 0.0 as acrescimo, 0.0 as desconto, 0.0 as valorrecebido , TIMESTAMP '2019-01-01 00:00:00' as dataterminoreserva,	TIMESTAMP '2019-01-01 00:00:00' as dataprevisaodevolucao,	TIMESTAMP '2019-01-01 00:00:00' as dataemprestimo,	TIMESTAMP '2019-01-01 00:00:00' as datadevolucao, '' as datarecebimentocontareceber, '' as datavencimentocontareceber");
			                           sqlStr.append(" from bloqueiobiblioteca ");
			                           sqlStr.append(" where bloqueiobiblioteca.pessoa = ").append(usuario.getPessoa().getCodigo());
			                           sqlStr.append(" ");
			                           sqlStr.append(" ");
			                           sqlStr.append(" union all");
			                           sqlStr.append(" select distinct 'BLOQUEIO_BIBLIOTECA_LIMITE' as tipoOrigemTimeLine, bloqueiobiblioteca.datalimitebloqueio as dataocorrencia, 5.1 as ordem, bloqueiobiblioteca.codigo as codigoorigem, bloqueiobiblioteca.motivobloqueio as titulo, '' codigobarra, 0.0 as valor, 0.0 as juro, 0.0 as multa, 0.0 as acrescimo, 0.0 as desconto, 0.0 as valorrecebido , TIMESTAMP '2019-01-01 00:00:00' as dataterminoreserva,	TIMESTAMP '2019-01-01 00:00:00' as dataprevisaodevolucao,	TIMESTAMP '2019-01-01 00:00:00' as dataemprestimo,	TIMESTAMP '2019-01-01 00:00:00' as datadevolucao, '' as datarecebimentocontareceber, '' as datavencimentocontareceber");
			                           sqlStr.append(" from bloqueiobiblioteca ");
			                           sqlStr.append(" where bloqueiobiblioteca.pessoa = ").append(usuario.getPessoa().getCodigo()).append(" and datalimitebloqueio is not null");	
			                           sqlStr.append(" ");
			                           sqlStr.append(" ");
			                           sqlStr.append(" order by dataocorrencia desc, ordem desc");
			                           
			                           
			                           SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			                         
			                           List<TimeLineVO> timeLineVO = new ArrayList<TimeLineVO>();
			                      Date dataAtual =  new Date();
			                   		while (tabelaResultado.next()) {
			                   			
			                   			
			                   			TimeLineVO obj = new TimeLineVO();
			                   			
			                   			obj.setTipoOrigemTimeLine(TipoOrigemTimeLineEnum.valueOf(tabelaResultado.getString("tipoOrigemTimeLine")));
			                   			obj.setDataOcorrenciaApresentar(Uteis.getDataJDBCTimestamp(tabelaResultado.getDate("dataocorrencia")));
			                   			obj.setCodigoOrigem(tabelaResultado.getString("codigoorigem"));
			                   			obj.setExemplar(tabelaResultado.getString("codigobarra"));
			                   			
			                   			obj.setCatalogo(tabelaResultado.getString("titulo"));
			                   			obj.setValorMulta(tabelaResultado.getDouble("valor"));
			                   			obj.setJuro(tabelaResultado.getDouble("juro"));
			                   			obj.setMulta(tabelaResultado.getDouble("multa"));
			                   			obj.setAcrescimo(tabelaResultado.getDouble("acrescimo"));
			                   			obj.setDesconto(tabelaResultado.getDouble("desconto"));
			                   			obj.setValorRecebido(tabelaResultado.getDouble("valorrecebido"));
			                   			obj.setDataTerminoReserva(Uteis.getData(tabelaResultado.getDate("dataterminoreserva"),"dd/MM/yyyy hh:mm"));
			                   			obj.setDataPrevDevolucao(Uteis.getData(tabelaResultado.getDate("dataprevisaodevolucao"),"dd/MM/yyyy hh:mm"));
			                   			obj.setDataDevolucao(Uteis.getData(tabelaResultado.getDate("datadevolucao"),"dd/MM/yyyy hh:mm"));
			                   			obj.setDataEmprestimo(Uteis.getData(tabelaResultado.getDate("dataemprestimo"),"dd/MM/yyyy hh:mm"));
			                   			obj.setDataVencimentoContaReceber(tabelaResultado.getString("dataVencimentoContaReceber"));
			                   			obj.setDataRecebimentoContaReceber(tabelaResultado.getString("dataRecebimentoContaReceber"));
			                   			if(obj.getTipoOrigemTimeLine().equals(TipoOrigemTimeLineEnum.EMPRESTIMO) || obj.getTipoOrigemTimeLine().equals(TipoOrigemTimeLineEnum.EMPRESTIMO_ATRASADO)
			                   				|| obj.getTipoOrigemTimeLine().equals(TipoOrigemTimeLineEnum.EMPRESTIMO_DEVOLVIDO_ATRASADO)){
			                   					if(Uteis.isAtributoPreenchido(tabelaResultado.getDate("datadevolucao"))) {
			                   						obj.setDiasAtraso(Long.valueOf(Uteis.nrDiasEntreDatas(tabelaResultado.getDate("datadevolucao"),tabelaResultado.getDate("dataprevisaodevolucao"))).intValue());
			                   					}else {
			                   						obj.setDiasAtraso(Long.valueOf(Uteis.nrDiasEntreDatas(dataAtual,tabelaResultado.getDate("dataprevisaodevolucao"))).intValue());
			                   					}
			                   			}
			                   			if(obj.getTipoOrigemTimeLine().equals(TipoOrigemTimeLineEnum.CONTA_RECEBER_PAGAMENTO) || obj.getTipoOrigemTimeLine().equals(TipoOrigemTimeLineEnum.CONTA_RECEBER_VENCIMENTO)){
				                   			if(Uteis.isAtributoPreenchido(tabelaResultado.getString("dataRecebimentoContaReceber"))) {
				                   				obj.setDiasAtraso(Long.valueOf(Uteis.nrDiasEntreDatas(Uteis.getData(tabelaResultado.getString("dataRecebimentoContaReceber"), "dd/MM/yyyy"),Uteis.getData(tabelaResultado.getString("dataVencimentoContaReceber"), "dd/MM/yyyy"))).intValue());				                   					
				                   			}else {
				                   				obj.setDiasAtraso(Long.valueOf(Uteis.nrDiasEntreDatas(dataAtual,Uteis.getData(tabelaResultado.getString("dataVencimentoContaReceber"), "dd/MM/yyyy"))).intValue());
				                   				}
				                   			}
			                   			if(obj.getDiasAtraso() < 0) {
			                   				obj.setDiasAtraso(0);
			                   			}
			                   			timeLineVO.add(obj);
			                   			
			                   		}
			
			                   		return timeLineVO;
		}
	}

	
	
