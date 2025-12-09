package negocio.facade.jdbc.academico;

import java.awt.Color;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.itextpdf.text.Font;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.Barcode128;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGraphics2D;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

import negocio.comuns.academico.IdentificacaoEstudantilVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.LayoutEtiquetaTagVO;
import negocio.comuns.basico.LayoutEtiquetaVO;
import negocio.comuns.basico.enumeradores.TagEtiquetaEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.interfaces.academico.IdentificacaoEstudantilInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
public class IdentificacaoEstudantil extends SuperRelatorio implements IdentificacaoEstudantilInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -713098063185634092L;

	public String designIReportRelatorioLayout1() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "IdentificacaoEstudantilRelLayout1" + ".jrxml");
	}

	public String designIReportRelatorioLayout2() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "IdentificacaoEstudantilRelLayout2" + ".jrxml");
	}

	public String designIReportRelatorioLayout3() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "IdentificacaoEstudantilRelLayout3" + ".jrxml");
	}

	@Override
	public String caminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public static String getIdEntidade() {
		return ("IdentificacaoEstudantilRel");
	}

	@Override
	public List<IdentificacaoEstudantilVO> consultarDadosIdentificacaoEstudantilAluno(Integer unidadeEnsino, Integer requerimento, Integer curso, Integer turma, Integer disciplina, String ano, String semestre, Boolean apenasRequerimentoPago, String matricula, Boolean utilizarFotoPerfilAluno, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append("select pessoa.nome as nomeAluno, pessoa.endereco, pessoa.cep, pessoa.setor, pessoa.rg, pessoa.dataNasc as dataNascimento, pessoa.cpf as cpf, pessoa.numero as numero, pessoa.complemento as complemento, ");
		sql.append("cidade.nome as cidade, estado.sigla as estado, arquivo.pastaBaseArquivo as pastaBaseArquivoPessoa, arquivo.nome as nomeArquivoPessoa , arquivo.codigo as codigoArquivoPessoa, ");
		sql.append("arquivoReq.pastaBaseArquivo as pastaBaseArquivoReq, arquivoReq.nome as nomeArquivoReq, arquivoReq.codigo as codigoArquivoReq, arquivoReq.cpfrequerimento as cpfRequerenteArquivo, matricula.matricula, curso.nome as nomeCurso, ");
		sql.append("unidadeEnsino.nome as unidadeEnsino, unidadeEnsino.endereco as enderecoUnidade, unidadeEnsino.setor as setorUnidade, unidadeEnsino.cep as cepUnidade, ");
		sql.append("cidadeUnidade.nome as cidadeUnidade, estadoUnidade.sigla as estadoUnidade, unidadeEnsino.telcomercial1 as telUnidade, ");
		sql.append("matriculaPeriodo.ano as anoLetivo, turma.identificadorTurma as turma, curso.nivelEducacional as nivelEnsino, matriculaPeriodo.semestre as semestre, ");
		sql.append("periodoletivo.descricao as periodo, turno.nome as turno, pessoa.pispasep as pis ");
		sql.append("from pessoa ");
		sql.append("inner join matricula on matricula.aluno = pessoa.codigo ");
		sql.append("inner join curso on curso.codigo = matricula.curso ");
		sql.append("inner join turno on turno.codigo = matricula.turno ");
		sql.append("inner join unidadeEnsino on unidadeEnsino.codigo = matricula.unidadeEnsino ");
		sql.append("inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula and matriculaPeriodo.codigo = (select mp.codigo from matriculaPeriodo mp where mp.matricula = matricula.matricula ");
		if (Uteis.isAtributoPreenchido(ano)) {
			sql.append(" and mp.ano =  '").append(ano).append("' ");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sql.append(" and mp.semestre =  '").append(semestre).append("' ");
		}
		sql.append("order by (mp.ano || '/' || mp.semestre) desc, case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc limit 1) ");
		sql.append("inner join turma on turma.codigo = matriculaPeriodo.turma ");
		sql.append("inner join periodoletivo on matriculaPeriodo.periodoletivomatricula = periodoletivo.codigo ");
		if (requerimento == null || requerimento == 0) {
			sql.append(" left join arquivo arquivoReq on arquivoReq.codigo = (");
			sql.append(" select max(arquivo) from requerimento inner join tiporequerimento on tiporequerimento.codigo = requerimento.tiporequerimento");
			sql.append(" where tiporequerimento.tipo = 'CE' and matricula.matricula = requerimento.matricula ) ");
		}
		if (requerimento != null && requerimento > 0) {
			sql.append(" inner join requerimento on requerimento.matricula = matricula.matricula ");
			sql.append(" inner join tiporequerimento on requerimento.tiporequerimento = tiporequerimento.codigo and tiporequerimento.tipo = 'CE' ");
			sql.append(" left join arquivo arquivoReq on requerimento.arquivo = arquivoReq.codigo ");
		} else if (apenasRequerimentoPago) {
			sql.append(" inner join requerimento on requerimento.matricula = matricula.matricula ");
		}
		sql.append("left join cidade cidadeUnidade on cidadeUnidade.codigo = unidadeEnsino.cidade ");
		sql.append("left join estado estadoUnidade on cidadeUnidade.estado = estadoUnidade.codigo ");
		sql.append("left join cidade on cidade.codigo = pessoa.cidade ");
		sql.append("left join estado on cidade.estado = estado.codigo ");
		sql.append("left join arquivo on arquivo.codigo = pessoa.arquivoImagem ");
		sql.append("WHERE matricula.situacao = 'AT' ");
		if (unidadeEnsino != null && unidadeEnsino > 0) {
			sql.append(" and unidadeEnsino.codigo =  ").append(unidadeEnsino);
		}
		if (requerimento != null && requerimento > 0) {
			sql.append(" and requerimento.codigo =  ").append(requerimento);
		}
		if (curso != null && curso > 0) {
			sql.append(" and curso.codigo =  ").append(curso);
		}
		if (turma != null && turma > 0) {
			sql.append(" and turma.codigo =  ").append(turma);
		}
		if (disciplina != null && disciplina > 0) {
			sql.append(" and matriculaPeriodo.codigo in (select distinct matriculaPeriodo from matriculaPeriodoTurmaDisciplina ");
			sql.append(" where matriculaPeriodoTurmaDisciplina.matriculaPeriodo = matriculaPeriodo.codigo  ");
			sql.append(" and matriculaPeriodoTurmaDisciplina.disciplina = ").append(disciplina).append(") ");
		}
		if (apenasRequerimentoPago) {
			sql.append(" and requerimento.situacao = 'PE' ");
			sql.append(" and (requerimento.situacaofinanceira = 'PG' or requerimento.situacaofinanceira = 'IS') ");
		}
		if (!matricula.equals("")) {
			sql.append(" and matricula.matricula = '").append(matricula).append("' ");
		}
		sql.append(" order by pessoa.nome ");
		List<IdentificacaoEstudantilVO> identificacaoEstudantilVOs = new ArrayList<IdentificacaoEstudantilVO>(0);
		IdentificacaoEstudantilVO obj = null;
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while (rs.next()) {
			File arquivoImagem = null;
			obj = new IdentificacaoEstudantilVO();
			obj.setNomeAluno(rs.getString("nomeAluno"));
			obj.setAnoLetivo(rs.getString("anoLetivo"));
			obj.setCep(rs.getString("cep"));
			obj.setCepUnidade(rs.getString("cepUnidade"));
			obj.setCidade(rs.getString("cidade"));
			obj.setCidadeUnidade(rs.getString("cidadeUnidade"));
			obj.setCpf(rs.getString("cpf"));
			if (utilizarFotoPerfilAluno && rs.getInt("codigoArquivoPessoa") > 0) {
				obj.setCodigoArquivo(rs.getInt("codigoArquivoPessoa"));
				obj.setNomeArquivo(rs.getString("nomeArquivoPessoa"));
				arquivoImagem = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.IMAGEM.getValue() + File.separator + rs.getString("cpf") + File.separator);
				if(arquivoImagem.exists()){
					obj.setPastaBaseArquivo(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.IMAGEM.getValue() + File.separator + rs.getString("cpf") + File.separator);
				}else{
					obj.setPastaBaseArquivo(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.IMAGEM.getValue() + File.separator);
				}
				arquivoImagem = new File(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + PastaBaseArquivoEnum.IMAGEM.getValue() + "/" + rs.getString("cpf") + "/" + obj.getNomeArquivo());
				if(arquivoImagem.exists()){
					obj.setPastaBaseArquivoWeb(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + PastaBaseArquivoEnum.IMAGEM.getValue() + "/" + rs.getString("cpf") + "/" + obj.getNomeArquivo());
				}else{
					obj.setPastaBaseArquivoWeb(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + PastaBaseArquivoEnum.IMAGEM.getValue() + "/" + rs.getString("cpf") + "/" + obj.getNomeArquivo());
				}
			} else if (rs.getInt("codigoArquivoReq") > 0) {
				obj.setCodigoArquivo(rs.getInt("codigoArquivoReq"));
				obj.setNomeArquivo(rs.getString("nomeArquivoReq"));
				arquivoImagem = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.REQUERIMENTOS.getValue() + File.separator + rs.getString("cpfRequerenteArquivo") + File.separator);
				if(arquivoImagem.exists()){
					obj.setPastaBaseArquivo(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.REQUERIMENTOS.getValue() + File.separator + rs.getString("cpfRequerenteArquivo") + File.separator);
				}else{
					obj.setPastaBaseArquivo(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.REQUERIMENTOS.getValue() + File.separator);
				}
				arquivoImagem = new File(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + PastaBaseArquivoEnum.REQUERIMENTOS.getValue() + "/" + rs.getString("cpfRequerenteArquivo") + "/" + obj.getNomeArquivo());
				if(arquivoImagem.exists()){
					obj.setPastaBaseArquivoWeb(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + PastaBaseArquivoEnum.REQUERIMENTOS.getValue() + "/" + rs.getString("cpfRequerenteArquivo") + "/" + obj.getNomeArquivo());
				}else{
					obj.setPastaBaseArquivoWeb(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + PastaBaseArquivoEnum.REQUERIMENTOS.getValue() + "/" + obj.getNomeArquivo());
				}
			} else {
				obj.setNomeArquivo("foto_usuario.png");
				obj.setPastaBaseArquivo(UteisJSF.getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "visao" + File.separator);
				obj.setPastaBaseArquivoWeb(UteisJSF.getCaminhoWeb() + "resources/imagens/visao/" + obj.getNomeArquivo());
			}
			obj.setFotoAluno(obj.getPastaBaseArquivo() + obj.getNomeArquivo());
			obj.setDataNascimento(Uteis.getData(rs.getDate("dataNascimento"), "dd/MM/yyyy"));
			obj.setEndereco(rs.getString("endereco"));
			obj.setEnderecoUnidade(rs.getString("enderecoUnidade"));
			obj.setEstado(rs.getString("estado"));
			obj.setEstadoUnidade(rs.getString("estadoUnidade"));
			obj.setMatricula(rs.getString("matricula"));
			obj.setNivelEnsino(rs.getString("nivelEnsino"));
			obj.setNomeCurso(rs.getString("nomeCurso"));
			obj.setPeriodo(rs.getString("periodo"));
			obj.setRg(rs.getString("rg"));
			obj.setSemestre(rs.getString("semestre"));
			obj.setSetor(rs.getString("setor"));
			obj.setSetorUnidade(rs.getString("setorUnidade"));
			obj.setTelUnidade(rs.getString("telUnidade"));
			obj.setTurma(rs.getString("turma"));
			obj.setTurno(rs.getString("turno"));
			obj.setUnidadeEnsino(rs.getString("unidadeEnsino"));
			obj.setPis(rs.getString("pis"));
			obj.setNumero(rs.getString("numero"));
			obj.setComplemento(rs.getString("complemento"));
            identificacaoEstudantilVOs.add(obj);
		}
		return identificacaoEstudantilVOs;
	}
	
	
	public List<IdentificacaoEstudantilVO> consultarDadosIdentificacaoEstudantilProfessor(Integer unidadeEnsino, Integer curso, String semestre, String ano, String matricula, Boolean utilizarFotoAluno, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("SELECT DISTINCT pessoa.nome as nomeProfessor, pessoa.endereco, pessoa.cep, pessoa.setor, pessoa.rg, pessoa.dataNasc as dataNascimento, pessoa.cpf as cpf,funcionario.matricula,curso.nome as nomeCurso, ");
		sqlStr.append(" cidade.nome as cidade, estado.sigla as estado, arquivo.pastaBaseArquivo as pastaBaseArquivoPessoa, arquivo.nome as nomeArquivoPessoa , arquivo.codigo as codigoArquivoPessoa, ");
 		sqlStr.append(" unidadeEnsino.nome as unidadeEnsino, unidadeEnsino.endereco as enderecoUnidade, unidadeEnsino.setor as setorUnidade, unidadeEnsino.cep as cepUnidade, ");
		sqlStr.append(" cidadeUnidade.nome as cidadeUnidade, estadoUnidade.sigla as estadoUnidade, unidadeEnsino.telcomercial1 as telUnidade, ");
		sqlStr.append(" curso.nivelEducacional as nivelEnsino, pessoa.pispasep as pis ");
		sqlStr.append("FROM horarioturma ");
		sqlStr.append(" INNER JOIN horarioturmadia     ON horarioturmadia.horarioturma        = horarioturma.codigo ");
		sqlStr.append(" INNER JOIN horarioturmadiaitem ON horarioturmadia.codigo        = horarioturmadiaitem.horarioturmadia ");
		sqlStr.append(" INNER JOIN turma               ON turma.codigo         = horarioturma.turma ");
		sqlStr.append(" INNER JOIN pessoa              ON pessoa.codigo        = horarioturmadiaitem.professor ");
		sqlStr.append(" INNER JOIN funcionario         ON funcionario.pessoa   = pessoa.codigo ");		
		sqlStr.append(" INNER JOIN curso               ON ((turma.turmaagrupada = false and curso.codigo = turma.curso) or (turma.turmaagrupada and curso.codigo in (select curso from turmaagrupada inner join turma as t on t.codigo = turmaagrupada.turma where turmaagrupada.turmaorigem = turma.codigo )))");
		sqlStr.append(" INNER JOIN unidadeensino       ON unidadeensino.codigo = turma.unidadeensino ");
		sqlStr.append(" INNER JOIN disciplina          ON disciplina.codigo    = horarioturmadiaitem.disciplina ");
		sqlStr.append(" LEFT JOIN cidade cidadeUnidade ON cidadeUnidade.codigo = unidadeEnsino.cidade ");
		sqlStr.append(" LEFT JOIN estado estadoUnidade ON cidadeUnidade.estado = estadoUnidade.codigo ");
		sqlStr.append(" LEFT JOIN cidade               ON cidade.codigo        = pessoa.cidade ");
		sqlStr.append(" LEFT JOIN estado               ON cidade.estado        = estado.codigo ");
		sqlStr.append(" LEFT JOIN arquivo              ON arquivo.codigo       = pessoa.arquivoImagem ");
		sqlStr.append("WHERE 1=1 ");

		if (Uteis.isAtributoPreenchido(matricula)) {
			sqlStr.append(" AND funcionario.matricula = '").append(matricula).append("'");
		}
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append("AND unidadeEnsino.codigo = ").append(unidadeEnsino);
		}
		if (Uteis.isAtributoPreenchido(curso)) {
			sqlStr.append(" AND curso.codigo = ").append(curso);
		}
		if (Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append(" AND horarioturma.anovigente = '").append(ano).append("'");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" AND horarioturma.semestrevigente = '").append(semestre).append("'");
		}

		sqlStr.append(" AND pessoa.ativo AND pessoa.professor ");

		sqlStr.append(" ORDER BY Pessoa.nome ;");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, usuario,utilizarFotoAluno,configuracaoGeralSistemaVO));
	}
	
	public static List<IdentificacaoEstudantilVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario, Boolean utilizarFotoAluno, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		List<IdentificacaoEstudantilVO> vetResultado = new ArrayList<IdentificacaoEstudantilVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, usuario, utilizarFotoAluno, configuracaoGeralSistemaVO));
		}
		return vetResultado;
	}

	public static IdentificacaoEstudantilVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario, Boolean utilizarFotoAluno, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		IdentificacaoEstudantilVO obj = new IdentificacaoEstudantilVO();
		File arquivoImagem = null;
		obj = new IdentificacaoEstudantilVO();
		obj.setNomeAluno(dadosSQL.getString("nomeProfessor"));
		obj.setCep(dadosSQL.getString("cep"));
		obj.setCepUnidade(dadosSQL.getString("cepUnidade"));
		obj.setCidade(dadosSQL.getString("cidade"));
		obj.setCidadeUnidade(dadosSQL.getString("cidadeUnidade"));
		obj.setCpf(dadosSQL.getString("cpf"));
		if (utilizarFotoAluno && dadosSQL.getInt("codigoArquivoPessoa") > 0) {
			obj.setCodigoArquivo(dadosSQL.getInt("codigoArquivoPessoa"));
			obj.setNomeArquivo(dadosSQL.getString("nomeArquivoPessoa"));
			arquivoImagem = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.IMAGEM.getValue() + File.separator + dadosSQL.getString("cpf") + File.separator);
			if (arquivoImagem.exists()) {
				obj.setPastaBaseArquivo(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.IMAGEM.getValue() + File.separator + dadosSQL.getString("cpf") + File.separator);
			} else {
				obj.setPastaBaseArquivo(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.IMAGEM.getValue() + File.separator);
			}
			arquivoImagem = new File(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + PastaBaseArquivoEnum.IMAGEM.getValue() + "/" + dadosSQL.getString("cpf") + "/" + obj.getNomeArquivo());
			if (arquivoImagem.exists()) {
				obj.setPastaBaseArquivoWeb(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + PastaBaseArquivoEnum.IMAGEM.getValue() + "/" + dadosSQL.getString("cpf") + "/" + obj.getNomeArquivo());
			} else {
				obj.setPastaBaseArquivoWeb(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + PastaBaseArquivoEnum.IMAGEM.getValue() + "/" + dadosSQL.getString("cpf") + "/" + obj.getNomeArquivo());
			}
		} else {
			obj.setNomeArquivo("foto_usuario.png");
			obj.setPastaBaseArquivo(UteisJSF.getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "visao" + File.separator);
			obj.setPastaBaseArquivoWeb(UteisJSF.getCaminhoWeb() + File.separator + "resources" + "/imagens/visao/" + obj.getNomeArquivo());
		}
		obj.setFotoAluno(obj.getPastaBaseArquivo() + obj.getNomeArquivo());
		obj.setDataNascimento(Uteis.getData(dadosSQL.getDate("dataNascimento"), "dd/MM/yyyy"));
		obj.setEndereco(dadosSQL.getString("endereco"));
		obj.setEnderecoUnidade(dadosSQL.getString("enderecoUnidade"));
		obj.setEstado(dadosSQL.getString("estado"));
		obj.setEstadoUnidade(dadosSQL.getString("estadoUnidade"));
		//Dados Funcionario
		obj.setMatricula(dadosSQL.getString("matricula"));
		obj.setNivelEnsino(dadosSQL.getString("nivelEnsino"));
		obj.setNomeCurso(dadosSQL.getString("nomeCurso"));
		obj.setRg(dadosSQL.getString("rg"));
		obj.setSetor(dadosSQL.getString("setor"));
		obj.setSetorUnidade(dadosSQL.getString("setorUnidade"));
		obj.setTelUnidade(dadosSQL.getString("telUnidade"));
		obj.setUnidadeEnsino(dadosSQL.getString("unidadeEnsino"));
		obj.setPis(dadosSQL.getString("pis"));

		return obj;
	}
	
	public static final float PONTO = 2.83f;
	
	public void realizarMotagemIdentificacaoPDF(LayoutEtiquetaVO layoutEtiqueta, List<IdentificacaoEstudantilVO> identificacaoEstudantilVOs ,ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,String caminho) throws Exception{
		Rectangle tamanhoPagina = null;
		Document pdf = null;
		FileOutputStream arquivo = null;
		try {
			float alturaPagina = layoutEtiqueta.getAlturaFolhaImpressao() * PONTO;
			float larguraPagina = layoutEtiqueta.getLarguraFolhaImpressao() * PONTO;
			float margemEsquerda = layoutEtiqueta.getMargemEsquerdaEtiquetaFolha().floatValue() * PONTO;
			float margemDireita = layoutEtiqueta.getMargemEsquerdaEtiquetaFolha().floatValue() * PONTO;
			float margemSuperior = layoutEtiqueta.getMargemSuperiorEtiquetaFolha().floatValue() * PONTO;
			float margemInferior = layoutEtiqueta.getMargemSuperiorEtiquetaFolha().floatValue() * PONTO;
			//StringBuffer caminhoDaImagem = new StringBuffer();
			File diretorio = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + PastaBaseArquivoEnum.LAYOUT_ETIQUETA.getValue());
			if (!diretorio.exists()) {
				diretorio.mkdirs();
			}
			diretorio = null;
			String nomeRel = caminho + File.separator + "relatorio" + File.separator + layoutEtiqueta.getModuloLayoutEtiqueta().name() +".pdf";
			//caminhoDaImagem.append(nomeRel);
			tamanhoPagina = new Rectangle(larguraPagina, alturaPagina);
			pdf = new Document(tamanhoPagina, margemEsquerda, margemDireita, margemSuperior, margemInferior);
			arquivo = new FileOutputStream(nomeRel);
			PdfWriter writer = PdfWriter.getInstance(pdf, arquivo);
			pdf.open();
			realizarMontagemPreviewEtiquetLaserTinta(layoutEtiqueta, writer, identificacaoEstudantilVOs, pdf);
		} catch (Exception e) {			
			throw e;
		} finally {

			if (pdf != null) {				
				pdf.close();
			}
			
			if (arquivo != null) {
				arquivo.flush();
				arquivo.close();
			}
		}
	}
	
	public void realizarMontagemPreviewEtiquetLaserTinta(LayoutEtiquetaVO layoutEtiqueta, PdfWriter writer, List<IdentificacaoEstudantilVO> identificacaoEstudantilVOs, Document pdf) throws Exception {
	    float alturaPagina = layoutEtiqueta.getAlturaFolhaImpressao() * PONTO;
			float larguraPagina = layoutEtiqueta.getLarguraFolhaImpressao() * PONTO;
			float margemEsquerda = layoutEtiqueta.getMargemEsquerdaEtiquetaFolha().floatValue() * PONTO;
			float margemSuperior = layoutEtiqueta.getMargemSuperiorEtiquetaFolha().floatValue() * PONTO;
			float alturaEtiqueta = layoutEtiqueta.getAlturaEtiqueta().floatValue() * PONTO;
			float larguraEtiqueta = layoutEtiqueta.getLarguraEtiqueta().floatValue() * PONTO;
			float margemHorizontalEntreEtiquetas = (layoutEtiqueta.getMargemEntreEtiquetaHorizontal() == 0 ? 0.2f : layoutEtiqueta.getMargemEntreEtiquetaHorizontal().floatValue()) * PONTO;
			float margemVerticalEntreEtiquetas = layoutEtiqueta.getMargemEntreEtiquetaVertical() * PONTO;
			float margemSuperiorColuna = 0f;
			float margemEsquerdaColuna = 0f;
			PdfContentByte canvas = writer.getDirectContent();
			PdfGraphics2D pdfEtiqueta = (PdfGraphics2D) writer.getDirectContent().createGraphics(larguraPagina, alturaPagina);
			Integer linhas = layoutEtiqueta.getNumeroLinhasEtiqueta();
			Integer colunas = layoutEtiqueta.getNumeroColunasEtiqueta();
			int linha = 1;
			int coluna = 1;
			try {
				for (IdentificacaoEstudantilVO identificacaoEstudantilVO : identificacaoEstudantilVOs) {
					margemSuperiorColuna = alturaPagina - margemSuperior - ((linha - 1) * margemHorizontalEntreEtiquetas) - ((linha - 1) * alturaEtiqueta);
					margemEsquerdaColuna = margemEsquerda + ((coluna - 1) * margemVerticalEntreEtiquetas) + ((coluna - 1) * larguraEtiqueta);
					pdfEtiqueta.draw(new RoundRectangle2D.Double(margemEsquerda + ((coluna - 1) * margemVerticalEntreEtiquetas) + ((coluna - 1) * larguraEtiqueta), margemSuperior + ((linha - 1) * margemHorizontalEntreEtiquetas) + ((linha - 1) * alturaEtiqueta), larguraEtiqueta, alturaEtiqueta, 0, 0));
					
					for (LayoutEtiquetaTagVO tag : layoutEtiqueta.getLayoutEtiquetaTagVOs()) {
						realizarMontagemPreviewLayoutEtiquetaTag(tag, canvas, alturaEtiqueta, larguraEtiqueta, margemSuperiorColuna, margemEsquerdaColuna, identificacaoEstudantilVO);
					}
					pdfEtiqueta.dispose();
					canvas.saveState();
					canvas.restoreState();
					if (coluna + 1 > colunas) {
						coluna = 1;
						if (linha + 1 > linhas) {
							pdf.newPage();
							linha = 1;
						} else {
							linha++;
						}
					} else {
						coluna++;
					}				
					//pdfEtiqueta.finalize();
				}
			} catch (Exception e) {
				pdfEtiqueta.dispose();
				canvas.saveState();
				canvas.restoreState();
				throw e;
			} finally {
				pdf.newPage();
			}
	}
	
	public void realizarMontagemPreviewLayoutEtiquetaTag(LayoutEtiquetaTagVO tag, PdfContentByte canvas, float alturaEtiqueta, float larguraEtiqueta, float margemSuperiorColuna, float margemEsquerdaColuna, IdentificacaoEstudantilVO identificacaoEstudantilVO) throws Exception {
		float margemEsquerdaLabel = tag.getMargemDireita() * PONTO;
		float margemSuperiorLabel = tag.getMargemTopo() * PONTO;
		PdfTemplate tmp = null;
		if (tag.getTagEtiqueta().equals(TagEtiquetaEnum.BIB_CODIGO_BARRAS)) { // entender
			tmp = canvas.createTemplate(larguraEtiqueta, alturaEtiqueta);
			tmp.beginText();
			Barcode128 barcode128 = new Barcode128();
			barcode128.setCodeType(Barcode128.CODE128);
			barcode128.setCode(tag.getValorTextoPreview());
			barcode128.setBarHeight(tag.getAlturaCodigoBarra());
			barcode128.setFont(BaseFont.createFont(FontFactory.TIMES_ROMAN, "", true));
			barcode128.setSize(tag.getTamanhoFonte().floatValue());
			if (!tag.getImprimirNumeroAbaixo()) {
				barcode128.setAltText("");
				barcode128.setStartStopText(tag.getImprimirNumeroAbaixo());
			}		
			tmp.addTemplate(barcode128.createTemplateWithBarcode(canvas, null, null), margemEsquerdaLabel, 0f);
			tmp.endText();			
			canvas.addTemplate(tmp, margemEsquerdaColuna, (margemSuperiorColuna - (margemSuperiorLabel)));
		} else if (tag.getTagEtiqueta().equals(TagEtiquetaEnum.IMAGEM_FUNDO)) {
			tmp = canvas.createTemplate(larguraEtiqueta, alturaEtiqueta);
			Image image = Image.getInstance(tag.getUrlImagem());
			image.setAbsolutePosition(0, 0);
			image.setAlignment(Element.ALIGN_CENTER);
			image.scaleToFit(larguraEtiqueta, alturaEtiqueta); // trabalha com a dimensaão original 
			tmp.addImage(image, true);
			canvas.addTemplate(tmp, margemEsquerdaColuna, margemSuperiorColuna - alturaEtiqueta);
		} else if (tag.getTagEtiqueta().equals(TagEtiquetaEnum.FOTO)) {
			tmp = canvas.createTemplate(larguraEtiqueta, alturaEtiqueta);
			Image img = Image.getInstance(identificacaoEstudantilVO.getFotoAluno());
			img.setAbsolutePosition(margemEsquerdaLabel, alturaEtiqueta - (tag.getAlturaFoto()*PONTO)  - margemSuperiorLabel);
			img.setAlignment(Element.ALIGN_CENTER);
			img.scaleAbsolute(tag.getLarguraFoto()*PONTO, tag.getAlturaFoto()*PONTO);
			tmp.addImage(img, true);		
			canvas.addTemplate(tmp, margemEsquerdaColuna, margemSuperiorColuna - alturaEtiqueta);
		} else {
			tmp = canvas.createTemplate(larguraEtiqueta, alturaEtiqueta);
			tmp.beginText();
			tmp.setColorFill(Color.decode(tag.getCorTexto()));
			BaseFont createFont = BaseFont.createFont(FontFactory.TIMES_ROMAN, "", true);
			if(tag.getFontNegrito()){
				tmp.setGrayStroke(Font.BOLD);
			}
			float tamanhoFonte = tag.getTamanhoFonte().floatValue();
			float larguraEtiquetaUtilizar = larguraEtiqueta - margemEsquerdaLabel;
			float larguraTexto = getFacadeFactory().getLayoutEtiquetaFacade().realizarCalculoLarguraTextoConformeTamanhoFonte(tamanhoFonte, createFont, (getValorImprimirEtiqueta(identificacaoEstudantilVO, tag)).trim());
			while (larguraTexto > larguraEtiquetaUtilizar) {
				larguraTexto = getFacadeFactory().getLayoutEtiquetaFacade().realizarCalculoLarguraTextoConformeTamanhoFonte(--tamanhoFonte, createFont, (getValorImprimirEtiqueta(identificacaoEstudantilVO, tag)).trim());
			}
			tmp.setFontAndSize(createFont, tamanhoFonte);
			tmp.showTextAligned(Element.ALIGN_LEFT, (getValorImprimirEtiqueta(identificacaoEstudantilVO, tag)).trim(), margemEsquerdaLabel, alturaEtiqueta - margemSuperiorLabel, tag.getRotacao() ? 90f : 0f);
			tmp.endText();
			canvas.addTemplate(tmp, margemEsquerdaColuna, margemSuperiorColuna - alturaEtiqueta);
		}
		//canvas.saveState();
	}
	
	private String getValorImprimirEtiqueta(IdentificacaoEstudantilVO item, LayoutEtiquetaTagVO tag){
		switch (tag.getTagEtiqueta()) {
		case NOME_PESSOA:
			if (tag.getApresentarLabelEtiquetaAposTagEtiqueta()) {
				return item.getNomeAluno().trim().isEmpty() ? "" : item.getNomeAluno().trim() + " " + tag.getLabelTag();
			} else {
				return item.getNomeAluno().trim().isEmpty() ? "" : tag.getLabelTag() + " " + item.getNomeAluno().trim();
			}
		case MATRICULA:
			if (tag.getApresentarLabelEtiquetaAposTagEtiqueta()) {
				return item.getMatricula().trim().isEmpty() ? "" : item.getMatricula().trim() + " " + tag.getLabelTag();
			} else {
				return item.getMatricula().trim().isEmpty() ? "" : tag.getLabelTag() + " " + item.getMatricula().trim();
			}
		case NOME_CURSO:
			if (tag.getApresentarLabelEtiquetaAposTagEtiqueta()) {
				return item.getNomeCurso().trim().isEmpty() ? "" : item.getNomeCurso().trim() + " " + tag.getLabelTag();
			} else {
				return item.getNomeCurso().trim().isEmpty() ? "" : tag.getLabelTag() + " " + item.getNomeCurso().trim();
			}
		case DATA_NASCIMENTO:
			if (tag.getApresentarLabelEtiquetaAposTagEtiqueta()) {
				return item.getDataNascimento().trim().isEmpty() ? "" : item.getDataNascimento().trim() + " " + tag.getLabelTag();
			} else {
				return item.getDataNascimento().trim().isEmpty() ? "" : tag.getLabelTag() + " " + item.getDataNascimento().trim();
			}
		case VALIDADE:
			if (item.getDataVencimento() == null) item.setDataVencimento("");
			if (tag.getApresentarLabelEtiquetaAposTagEtiqueta()) {
				return item.getDataVencimento().trim().isEmpty() ? "" : item.getDataVencimento().trim() + " " + tag.getLabelTag();
			} else {
				return item.getDataVencimento().trim().isEmpty() ? "" : tag.getLabelTag() + " " + item.getDataVencimento().trim();
			}
		case EXPEDICAO:
			if (tag.getApresentarLabelEtiquetaAposTagEtiqueta()) {
				return item.getOrgaoResponsavelEmissaoCarteirinha().trim().isEmpty() ? "" : item.getOrgaoResponsavelEmissaoCarteirinha().trim() + " " + tag.getLabelTag();
			} else {
				return item.getOrgaoResponsavelEmissaoCarteirinha().trim().isEmpty() ? "" : tag.getLabelTag() + " " + item.getOrgaoResponsavelEmissaoCarteirinha().trim();
			}
		case RG_PESSOA:
			if (tag.getApresentarLabelEtiquetaAposTagEtiqueta()) {
				return item.getRg().trim().isEmpty() ? "" : item.getRg().trim() + " " + tag.getLabelTag();
			} else {
				return item.getRg().trim().isEmpty() ? "" : tag.getLabelTag() + " " + item.getRg().trim();
			}
		case CPF_PESSOA:
			if (tag.getApresentarLabelEtiquetaAposTagEtiqueta()) {
				return item.getCpf().trim().isEmpty() ? "" : item.getCpf().trim() + " " + tag.getLabelTag();
			} else {
				return item.getCpf().trim().isEmpty() ? "" : tag.getLabelTag() + " " + item.getCpf().trim();
			}
		case PIS:
			if (tag.getApresentarLabelEtiquetaAposTagEtiqueta()) {
				return item.getPis().trim().isEmpty() ? "" : item.getPis().trim() + " " + tag.getLabelTag();
			} else {
				return item.getPis().trim().isEmpty() ? "" : tag.getLabelTag() + " " + item.getPis().trim();
			}
		case TITULO:
			if (tag.getApresentarLabelEtiquetaAposTagEtiqueta()) {
				return item.getTituloDaIdentificacao().trim().isEmpty() ? "" : item.getTituloDaIdentificacao().trim() + " " + tag.getLabelTag();
			} else {
				return item.getTituloDaIdentificacao().trim().isEmpty() ? "" : tag.getLabelTag() + " " + item.getTituloDaIdentificacao().trim();
			}
		case PERIODO_LETIVO:
			if (tag.getApresentarLabelEtiquetaAposTagEtiqueta()) {
				return item.getPeriodo().trim().isEmpty() ? "" : item.getPeriodo().trim() + " " + tag.getLabelTag();
			} else {
				return item.getPeriodo().trim().isEmpty() ? "" : tag.getLabelTag() + " " + item.getPeriodo().trim(); 
			}
			
		case TURNO:
			if (tag.getApresentarLabelEtiquetaAposTagEtiqueta()) {
				return item.getTurno().trim().isEmpty() ? "" : item.getTurno().trim() + " " + tag.getLabelTag();
			} else {
				return item.getTurno().trim().isEmpty() ? "" : tag.getLabelTag() + " " + item.getTurno().trim(); 
			}
			
		case TURMA:
			if (tag.getApresentarLabelEtiquetaAposTagEtiqueta()) {
				return item.getTurma().trim().isEmpty() ? "" : item.getTurma().trim() + " " + tag.getLabelTag();
			} else {
				return item.getTurma().trim().isEmpty() ? "" : tag.getLabelTag() + " " + item.getTurma().trim(); 
			}
			
		case ANO:
			if (tag.getApresentarLabelEtiquetaAposTagEtiqueta()) {
				return item.getAnoLetivo().trim().isEmpty() ? "" : item.getAnoLetivo().trim() + " " + tag.getLabelTag();
			} else {
				return item.getAnoLetivo().trim().isEmpty() ? "" : tag.getLabelTag() + " " + item.getAnoLetivo().trim(); 
			}
			
		case SEMESTRE:
			if (tag.getApresentarLabelEtiquetaAposTagEtiqueta()) {
				return item.getSemestre().trim().isEmpty() ? "" : item.getSemestre().trim() + " " + tag.getLabelTag();
			} else {
				return item.getSemestre().trim().isEmpty() ? "" : tag.getLabelTag() + " " + item.getSemestre().trim(); 
			}
			
		case ENDERECO:
			if (tag.getApresentarLabelEtiquetaAposTagEtiqueta()) {
				return item.getEndereco().trim().isEmpty() ? "" : item.getEndereco().trim() + " " + tag.getLabelTag();
			} else {
				return item.getEndereco().trim().isEmpty() ? "" : tag.getLabelTag() + " " + item.getEndereco().trim(); 
			}
			
		case NUMERO:
			if (tag.getApresentarLabelEtiquetaAposTagEtiqueta()) {
				return item.getNumero().trim().isEmpty() ? "" : item.getNumero().trim() + " " + tag.getLabelTag();
			} else {
				return item.getNumero().trim().isEmpty() ? "" : tag.getLabelTag() + " " + item.getNumero().trim(); 
			}
			
		case COMPLEMENTO:
			if (tag.getApresentarLabelEtiquetaAposTagEtiqueta()) {
				return item.getComplemento().trim().isEmpty() ? "" : item.getComplemento().trim() + " " + tag.getLabelTag();
			} else {
				return item.getComplemento().trim().isEmpty() ? "" : tag.getLabelTag() + " " + item.getComplemento().trim(); 
			}
			
		case BAIRRO:
			if (tag.getApresentarLabelEtiquetaAposTagEtiqueta()) {
				return item.getSetor().trim().isEmpty() ? "" : item.getSetor().trim() + " " + tag.getLabelTag();
			} else {
				return item.getSetor().trim().isEmpty() ? "" : tag.getLabelTag() + " " + item.getSetor().trim(); 
			}
			
		case CIDADE:
			if (tag.getApresentarLabelEtiquetaAposTagEtiqueta()) {
				return item.getCidade().trim().isEmpty() ? "" : item.getCidade().trim() + " " + tag.getLabelTag();
			} else {
				return item.getCidade().trim().isEmpty() ? "" : tag.getLabelTag() + " " + item.getCidade().trim(); 
			}
			
		case ESTADO:
			if (tag.getApresentarLabelEtiquetaAposTagEtiqueta()) {
				return item.getEstado().trim().isEmpty() ? "" : item.getEstado().trim() + " " + tag.getLabelTag();
			} else {
				return item.getEstado().trim().isEmpty() ? "" : tag.getLabelTag() + " " + item.getEstado().trim(); 
			}

			
		default:
			return "";
		}
	}
}
