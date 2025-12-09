package negocio.facade.jdbc.biblioteca;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.ArquivoMarc21CatalogoVO;
import negocio.comuns.biblioteca.ArquivoMarc21VO;
import negocio.comuns.biblioteca.AutorVO;
import negocio.comuns.biblioteca.CatalogoAutorVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.biblioteca.CidadePublicacaoCatalogoVO;
import negocio.comuns.biblioteca.EditoraVO;
import negocio.comuns.biblioteca.LinguaPublicacaoCatalogoVO;
import negocio.comuns.biblioteca.TipoCatalogoVO;
import negocio.comuns.biblioteca.enumeradores.CodigoIdiomaEnum;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.biblioteca.ArquivoMarc21InterfaceFacade;

import org.marc4j.MarcReader;
import org.marc4j.MarcStreamReader;
import org.marc4j.MarcStreamWriter;
import org.marc4j.MarcWriter;
import org.marc4j.MarcXmlParser;
import org.marc4j.MarcXmlReader;
import org.marc4j.MarcXmlWriter;
import org.marc4j.converter.impl.AnselToUnicode;
import org.marc4j.marc.ControlField;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Leader;
import org.marc4j.marc.MarcFactory;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;

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
import org.xml.sax.InputSource;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.ArquivoMarc21CatalogoVO;
import negocio.comuns.biblioteca.ArquivoMarc21VO;
import negocio.comuns.biblioteca.AutorVO;
import negocio.comuns.biblioteca.CatalogoAutorVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.biblioteca.CidadePublicacaoCatalogoVO;
import negocio.comuns.biblioteca.EditoraVO;
import negocio.comuns.biblioteca.LinguaPublicacaoCatalogoVO;
import negocio.comuns.biblioteca.TipoCatalogoVO;
import negocio.comuns.biblioteca.enumeradores.CodigoIdiomaEnum;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.biblioteca.ArquivoMarc21InterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class ArquivoMarc21 extends ControleAcesso implements ArquivoMarc21InterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public ArquivoMarc21() throws Exception {
		super();
		setIdEntidade("ArquivoMarc21");
	}

	public void executarExportarArquivoMarc21(ArquivoMarc21VO arquivoMarc21VO, ConfiguracaoGeralSistemaVO conSistemaVO, UsuarioVO usuarioVO) throws Exception {
		try {
			arquivoMarc21VO.getArquivoVO().setPastaBaseArquivo(PastaBaseArquivoEnum.ARQUIVO_TMP.getValue());
			arquivoMarc21VO.getArquivoVO().setNome(ArquivoHelper.criarNomeArquivo(usuarioVO, "txt"));
			OutputStream out = new FileOutputStream(conSistemaVO.getLocalUploadArquivoFixo() + File.separator + arquivoMarc21VO.getArquivoVO().getPastaBaseArquivo() + File.separator + arquivoMarc21VO.getArquivoVO().getNome());
			MarcFactory marcFactory = MarcFactory.newInstance();
			MarcWriter writer = new MarcStreamWriter(out, "UTF8");
			for (ArquivoMarc21CatalogoVO marc21CatalogoVO : arquivoMarc21VO.getArquivoMarc21CatalogoVOs()) {
				CatalogoVO catalogoVO = marc21CatalogoVO.getCatalogoVO();
				if (catalogoVO.getNumeroControle().trim().equals("") || catalogoVO.getNumeroControle() == null) {
					throw new Exception("O NUMERO DE CONTROLE deve ser preenchido para exportar o MARC.");
				}
					Record record = marcFactory.newRecord();
					record.addVariableField(marcFactory.newControlField("001", catalogoVO.getNumeroControle()));
					adicionarFieldTituloPrincipal(catalogoVO, marcFactory, record);
					adicionarFieldClassificacao(catalogoVO, marcFactory, record);
					adicionarFieldImprenta(catalogoVO, marcFactory, record);
					adicionarFieldAutor(catalogoVO, marcFactory, record);
					adicionarFieldEdicao(catalogoVO, marcFactory, record);
					adicionarFieldIdioma(catalogoVO, marcFactory, record);
					adicionarFieldAcessoEletronico(catalogoVO, marcFactory, record);
					adicionarFieldAssunto(catalogoVO, marcFactory, record);
					adicionarFieldISBN(catalogoVO, marcFactory, record);
					adicionarFieldDescricaoFisica(catalogoVO, marcFactory, record);
					adicionarFieldISSN(catalogoVO, marcFactory, record);
					adicionarFieldFormasVariantesTitulo(catalogoVO, marcFactory, record);
					adicionarFieldLocalizacao(catalogoVO, marcFactory, record);
					writer.write(record);
				
				}
			writer.close();
		} catch (Exception e) {
			throw e;
		}
	}
	
	

	public void adicionarFieldTituloPrincipal(CatalogoVO catalogoVO, MarcFactory marcFactory, Record record) throws Exception {
		DataField field = marcFactory.newDataField("245", "1".charAt(0), "0".charAt(0));
		field.addSubfield(marcFactory.newSubfield("a".charAt(0), catalogoVO.getTitulo()));
		if (!catalogoVO.getSubtitulo().equals("")) {
			field.addSubfield(marcFactory.newSubfield("b".charAt(0), catalogoVO.getSubtitulo()));
		}
		if (!catalogoVO.getTipoCatalogo().getCodigo().equals(0)) {
			field.addSubfield(marcFactory.newSubfield("h".charAt(0), catalogoVO.getTipoCatalogo().getNome()));
		}
		record.addVariableField(field);
	}

	public void adicionarFieldClassificacao(CatalogoVO catalogoVO, MarcFactory marcFactory, Record record) throws Exception {
		if (!catalogoVO.getClassificacaoBibliografica().equals("")) {
			DataField field = marcFactory.newDataField("082", "0".charAt(0), "4".charAt(0));
			field.addSubfield(marcFactory.newSubfield("2".charAt(0), catalogoVO.getEdicao()));
			field.addSubfield(marcFactory.newSubfield("a".charAt(0), catalogoVO.getClassificacaoBibliografica()));
			record.addVariableField(field);
		}
	}

	public void adicionarFieldImprenta(CatalogoVO catalogoVO, MarcFactory marcFactory, Record record) throws Exception {
		if (!catalogoVO.getCidadePublicacaoCatalogo().getCodigo().equals(0) || !catalogoVO.getEditora().getCodigo().equals(0) || !catalogoVO.getAnoPublicacao().equals("")) {
			DataField field = marcFactory.newDataField("260", "0".charAt(0), "0".charAt(0));
			if (!catalogoVO.getCidadePublicacaoCatalogo().getCodigo().equals(0)) {
				field.addSubfield(marcFactory.newSubfield("a".charAt(0), catalogoVO.getCidadePublicacaoCatalogo().getNome()));
			}
			if (!catalogoVO.getEditora().getCodigo().equals(0)) {
				field.addSubfield(marcFactory.newSubfield("b".charAt(0), catalogoVO.getEditora().getNome()));
			}
			if (!catalogoVO.getAnoPublicacao().equals("")) {
				field.addSubfield(marcFactory.newSubfield("c".charAt(0), catalogoVO.getAnoPublicacao()));
			}
			record.addVariableField(field);
		}
	}

	public void adicionarFieldAutor(CatalogoVO catalogoVO, MarcFactory marcFactory, Record record) throws Exception {
		if (!catalogoVO.getCatalogoAutorVOs().isEmpty()) {
			DataField field = marcFactory.newDataField("100", "1".charAt(0), "0".charAt(0));
			field.addSubfield(marcFactory.newSubfield("a".charAt(0), catalogoVO.getCatalogoAutorVOs().get(0).getAutor().getNome()));
			if (!catalogoVO.getCatalogoAutorVOs().get(0).getAutor().getAnoNascimento().equals("") && !catalogoVO.getCatalogoAutorVOs().get(0).getAutor().getAnoFalecimento().equals("")) {
				field.addSubfield(marcFactory.newSubfield("d".charAt(0), catalogoVO.getCatalogoAutorVOs().get(0).getAutor().getAnoNascimento() + "-" + catalogoVO.getCatalogoAutorVOs().get(0).getAutor().getAnoFalecimento()));
			}
			record.addVariableField(field);
		}
	}

	public void adicionarFieldEdicao(CatalogoVO catalogoVO, MarcFactory marcFactory, Record record) throws Exception {
		if (!catalogoVO.getEdicao().equals("")) {
			DataField field = marcFactory.newDataField("250", "0".charAt(0), "0".charAt(0));
			field.addSubfield(marcFactory.newSubfield("a".charAt(0), catalogoVO.getEdicao()));
			record.addVariableField(field);
		}
	}
	
	public void adicionarFieldIdioma(CatalogoVO catalogoVO, MarcFactory marcFactory, Record record) throws Exception {
		if (!catalogoVO.getLinguaPublicacaoCatalogo().getCodigo().equals(0) && !catalogoVO.getLinguaPublicacaoCatalogo().getMarcCode().equals("")) {
			DataField field = marcFactory.newDataField("041", "0".charAt(0), "0".charAt(0));
			field.addSubfield(marcFactory.newSubfield("a".charAt(0), catalogoVO.getLinguaPublicacaoCatalogo().getMarcCode()));
			record.addVariableField(field);
		}
	}
	
	public void adicionarFieldAcessoEletronico(CatalogoVO catalogoVO, MarcFactory marcFactory, Record record) throws Exception {
		if (!catalogoVO.getLink().equals("")) {
			DataField field = marcFactory.newDataField("856", "0".charAt(0), "0".charAt(0));
			field.addSubfield(marcFactory.newSubfield("u".charAt(0), catalogoVO.getLink()));
			record.addVariableField(field);
		}
	}
	
	public void adicionarFieldAssunto(CatalogoVO catalogoVO, MarcFactory marcFactory, Record record) throws Exception {
		if (!catalogoVO.getAssunto().equals("")) {
			for (String assunto : catalogoVO.getAssunto().split(",")) {
				DataField field = marcFactory.newDataField("600", "2".charAt(0), "4".charAt(0));
				field.addSubfield(marcFactory.newSubfield("a".charAt(0), assunto));
				record.addVariableField(field);
			}
		}
	}
	
	public void adicionarFieldISBN(CatalogoVO catalogoVO, MarcFactory marcFactory, Record record) throws Exception {
		if (!catalogoVO.getIsbn().equals("")) {
			DataField field = marcFactory.newDataField("020", "0".charAt(0), "0".charAt(0));
			field.addSubfield(marcFactory.newSubfield("a".charAt(0), catalogoVO.getIsbn()));
			record.addVariableField(field);
		}
	}
	
	public void adicionarFieldDescricaoFisica(CatalogoVO catalogoVO, MarcFactory marcFactory, Record record) throws Exception {
		if (!catalogoVO.getNrPaginas().equals(0)) {
			DataField field = marcFactory.newDataField("300", "0".charAt(0), "0".charAt(0));
			field.addSubfield(marcFactory.newSubfield("a".charAt(0), catalogoVO.getNrPaginas().toString()));
			record.addVariableField(field);
		}
	}
	
	public void adicionarFieldISSN(CatalogoVO catalogoVO, MarcFactory marcFactory, Record record) throws Exception {
		if (!catalogoVO.getIssn().equals("")) {
			DataField field = marcFactory.newDataField("022", "0".charAt(0), "0".charAt(0));
			field.addSubfield(marcFactory.newSubfield("a".charAt(0), catalogoVO.getIssn()));
			record.addVariableField(field);
		}
	}
	
	public void adicionarFieldFormasVariantesTitulo(CatalogoVO catalogoVO, MarcFactory marcFactory, Record record) throws Exception {
		if (!catalogoVO.getAbreviacaoTitulo().equals("")) {
			DataField field = marcFactory.newDataField("246", "0".charAt(0), "0".charAt(0));
			field.addSubfield(marcFactory.newSubfield("a".charAt(0), catalogoVO.getAbreviacaoTitulo()));
			record.addVariableField(field);
		}
	}
	
	public void adicionarFieldLocalizacao(CatalogoVO catalogoVO, MarcFactory marcFactory, Record record) throws Exception {
		if (!catalogoVO.getTipoCatalogo().getNome().equals("")) {
			DataField field = marcFactory.newDataField("852", "_".charAt(0), "_".charAt(0));
			field.addSubfield(marcFactory.newSubfield("a".charAt(0), catalogoVO.getTipoCatalogo().getNome()));
			record.addVariableField(field);
		}
	}

	public synchronized void executarProcessarArquivoMarc21(ArquivoMarc21VO arquivoMarc21VO, ConfiguracaoGeralSistemaVO conSistemaVO, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(arquivoMarc21VO);
			ArquivoMarc21CatalogoVO marc21CatalogoVO = null;
			File arquivoMarc21 = null;
			if (arquivoMarc21VO.getArquivoVO().getCodigo().equals(0)) {
				arquivoMarc21 = new File(conSistemaVO.getLocalUploadArquivoFixo() + File.separator + arquivoMarc21VO.getArquivoVO().getPastaBaseArquivo() + File.separator + arquivoMarc21VO.getArquivoVO().getNome());
			} else {
				arquivoMarc21 = new File(arquivoMarc21VO.getArquivoVO().getPastaBaseArquivo() + File.separator + arquivoMarc21VO.getArquivoVO().getNome());
			}
			InputStream in = new FileInputStream(arquivoMarc21);
			MarcReader reader = new MarcStreamReader(in, "UTF8");
			while (reader.hasNext()) {
				marc21CatalogoVO = new ArquivoMarc21CatalogoVO();
				Record record = null;
				try {
					record = reader.next();
				} catch (Exception e) {
					break;
				}
				ControlField numeroControle = (ControlField) record.getVariableField("001");
				DataField title = (DataField) record.getVariableField("245");
				if (!getFacadeFactory().getCatalogoFacade().verificarExisteCatalogoPorNumeroControle(numeroControle.getData(), title.getSubfield("a".charAt(0)).getData(), usuarioVO)) {
					/* Contém o número de controle atribuído pela organização criadora */
					marc21CatalogoVO.getCatalogoVO().setNumeroControle(numeroControle.getData());

					List<DataField> fields = record.getDataFields();
					Iterator<DataField> i = fields.iterator();
					while (i.hasNext()) {
						DataField field = i.next();
						if (field.getTag().equals("245")) {
							montarDadosTituloPrincipal(marc21CatalogoVO.getCatalogoVO(), field, usuarioVO);
						} else if (field.getTag().equals("082")) {
							montarDadosClassificacao(marc21CatalogoVO.getCatalogoVO(), field, usuarioVO);
						} else if (field.getTag().equals("260")) {
							montarDadosImprenta(marc21CatalogoVO.getCatalogoVO(), field, usuarioVO);
						} else if (field.getTag().equals("100")) {
							montarDadosAutor(marc21CatalogoVO.getCatalogoVO(), field, usuarioVO);
						} else if (field.getTag().equals("250")) {
							montarDadosEdicao(marc21CatalogoVO.getCatalogoVO(), field, usuarioVO);
						} else if (field.getTag().equals("041")) {
							montarDadosIdioma(marc21CatalogoVO.getCatalogoVO(), field, usuarioVO);
						} else if (field.getTag().equals("856")) {
							montarDadosAcessoEletronico(marc21CatalogoVO.getCatalogoVO(), field, usuarioVO);
						} else if (field.getTag().equals("600") || field.getTag().equals("610") || field.getTag().equals("611") || field.getTag().equals("630") 
								|| field.getTag().equals("648") || field.getTag().equals("650") || field.getTag().equals("651")) { 
							montarDadosAssunto(marc21CatalogoVO.getCatalogoVO(), field, usuarioVO);
						} else if (field.getTag().equals("020")) {
							montarDadosISBN(marc21CatalogoVO.getCatalogoVO(), field, usuarioVO);
						}  else if (field.getTag().equals("300")) {
							montarDadosDescricaoFisica(marc21CatalogoVO.getCatalogoVO(), field, usuarioVO);
						} else if (field.getTag().equals("022")) {
							montarDadosISSN(marc21CatalogoVO.getCatalogoVO(), field, usuarioVO);
						} else if (field.getTag().equals("246")) {
							montarDadosFormasVariantesTitulo(marc21CatalogoVO.getCatalogoVO(), field, usuarioVO);
						} else if (field.getTag().equals("852")) {
							montarDadosLocalizacao(marc21CatalogoVO.getCatalogoVO(), field, usuarioVO);
						}
					}
					arquivoMarc21VO.getArquivoMarc21CatalogoVOs().add(marc21CatalogoVO);
					marc21CatalogoVO = null;
				} else {
					if (marc21CatalogoVO.getCatalogoVO().getAssinaturaPeriodico()) {
						throw new Exception(UteisJSF.internacionalizar("msg_Biblioteca_ErroPeriodicoJaCadastrado").replace("{0}", title.getSubfield("a".charAt(0)).getData()));
					} else {
						throw new Exception(UteisJSF.internacionalizar("msg_Biblioteca_ErroCatalogoJaCadastrado").replace("{0}", title.getSubfield("a".charAt(0)).getData()));
					}
				}
			}
			arquivoMarc21 = null;
			in.close();
			reader = null;
		} catch (Exception e) {
			throw e;
		}
	}

	public static void montarDadosTituloPrincipal(CatalogoVO catalogoVO, DataField field, UsuarioVO usuarioVO) throws Exception {
		if (field != null) {
			List<Subfield> subfields = field.getSubfields();
			Iterator<Subfield> i = subfields.iterator();
			while (i.hasNext()) {
				Subfield subfield = i.next();
				String code = String.valueOf(subfield.getCode());
				String data = subfield.getData();
				if (code.equals("a")) {
					catalogoVO.setTitulo(data);
				} else if (code.equals("b")) {
					catalogoVO.setSubtitulo(data);
				} else if (code.equals("h")) {
					TipoCatalogoVO tipoCatalogoVO = getFacadeFactory().getTipoCatalogoFacade().consultarPorNomeRegistroUnico(data, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
					if (!Uteis.isAtributoPreenchido(tipoCatalogoVO.getCodigo())) {
						tipoCatalogoVO.setNome(data);
						getFacadeFactory().getTipoCatalogoFacade().incluir(tipoCatalogoVO, usuarioVO);
					}
					catalogoVO.setTipoCatalogo(tipoCatalogoVO);
					if (Uteis.removerAcentos(catalogoVO.getTipoCatalogo().getNome()).toUpperCase().contains("PERIODICO")) {
						catalogoVO.setAssinaturaPeriodico(true);
					}
				}
			}
		}
	}

	public static void montarDadosClassificacao(CatalogoVO catalogoVO, DataField field, UsuarioVO usuarioVO) throws Exception {
		if (field != null) {
			List<Subfield> subfields = field.getSubfields();
			Iterator<Subfield> i = subfields.iterator();
			while (i.hasNext()) {
				Subfield subfield = i.next();
				String code = String.valueOf(subfield.getCode());
				String data = subfield.getData();
				if (code.equals("a")) {
					catalogoVO.setClassificacaoBibliografica(data);
				}
				if (code.equals("2")) {
					catalogoVO.setEdicao(data);
				}
			}
		}
	}

	public static void montarDadosImprenta(CatalogoVO catalogoVO, DataField field, UsuarioVO usuarioVO) throws Exception {
		if (field != null) {
			List<Subfield> subfields = field.getSubfields();
			Iterator<Subfield> i = subfields.iterator();
			while (i.hasNext()) {
				Subfield subfield = i.next();
				String code = String.valueOf(subfield.getCode());
				String data = subfield.getData();
				if (code.equals("a")) {
					String cidade = data.trim();
					if (cidade.endsWith(".") || cidade.endsWith(":") || cidade.endsWith(";")) {
						cidade = cidade.substring(0, cidade.length() -1);
						cidade = cidade.trim();
					}
					CidadePublicacaoCatalogoVO cidadePublicacaoCatalogoVO = getFacadeFactory().getCidadePublicacaoCatalogoFacade().consultarPorNomeRegistroUnico(cidade, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
					if (cidadePublicacaoCatalogoVO.getCodigo().equals(0)) {
						cidadePublicacaoCatalogoVO.setNome(cidade);
						getFacadeFactory().getCidadePublicacaoCatalogoFacade().incluir(cidadePublicacaoCatalogoVO, usuarioVO);
					}
					catalogoVO.setCidadePublicacaoCatalogo(cidadePublicacaoCatalogoVO);
				} else if (code.equals("b")) {
					String editora = data.trim();
					if (editora.endsWith(",") || editora.endsWith(":") || editora.endsWith(";")) {
						editora = editora.substring(0, editora.length() - 1);
						editora = editora.trim();
					}
					EditoraVO editoraVO = getFacadeFactory().getEditoraFacade().consultarPorNomeRegistroUnico(editora, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
					if (editoraVO.getCodigo().equals(0)) {
						editoraVO.setNome(editora);
						getFacadeFactory().getEditoraFacade().incluir(editoraVO, usuarioVO);
					}
					catalogoVO.setEditora(editoraVO);
				} else if (code.equals("c")) {
					catalogoVO.setAnoPublicacao(Uteis.removeCaractersEspeciais(data));
				}
			}
		}
	}

	public static void montarDadosAutor(CatalogoVO catalogoVO, DataField field, UsuarioVO usuarioVO) throws Exception {
		if (field != null) {
			AutorVO autorVO = null;
			CatalogoAutorVO catalogoAutorVO = new CatalogoAutorVO();
			List<Subfield> subfields = field.getSubfields();
			Iterator<Subfield> i = subfields.iterator();
			while (i.hasNext()) {
				Subfield subfield = i.next();
				String code = String.valueOf(subfield.getCode());
				String data = subfield.getData();
				if (code.equals("a")) {
					String nomeAutor = data.trim();
					if (nomeAutor.endsWith(",")) {
						nomeAutor = nomeAutor.substring(0, nomeAutor.length() -1);
						nomeAutor = nomeAutor.trim();
					}
					autorVO = getFacadeFactory().getAutorFacade().consultarPorNomeRegistroUnico(nomeAutor, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);
					if (autorVO.getCodigo().equals(0)) {
						autorVO.setNome(nomeAutor);
					}
				} else if (code.equals("d")) {
					if (autorVO.getCodigo().equals(0)) {
						String[] anoNascimento = data.split("-");
						if (anoNascimento.length == 2) {
							if (anoNascimento[0].length() >= 4 ) {
								autorVO.setAnoNascimento(anoNascimento[0].substring(0,4));
							}
						    if (anoNascimento[1].length() >= 4) {
						    	autorVO.setAnoFalecimento(anoNascimento[1].substring(0,4));
						    }
						}
					}
				}
			}
			if (Uteis.isAtributoPreenchido(autorVO)) {
				
				if (Uteis.isAtributoPreenchido(autorVO.getCodigo())) {
					catalogoAutorVO = getFacadeFactory().getCatalogoAutorFacade().consultarPorCodigoAutorRegistroUnico(autorVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
				}
				
				if (autorVO != null && autorVO.getCodigo().equals(0)) {
					getFacadeFactory().getAutorFacade().incluir(autorVO, usuarioVO);
				}
				catalogoAutorVO.setAutor(autorVO);
				catalogoVO.getCatalogoAutorVOs().add(catalogoAutorVO);
			}
		}
	}

	public static void montarDadosEdicao(CatalogoVO catalogoVO, DataField field, UsuarioVO usuarioVO) throws Exception {
		if (field != null) {
			List<Subfield> subfields = field.getSubfields();
			Iterator<Subfield> i = subfields.iterator();
			while (i.hasNext()) {
				Subfield subfield = i.next();
				String code = String.valueOf(subfield.getCode());
				String data = subfield.getData();
				if (code.equals("a")) {
					catalogoVO.setEdicao(data);
					return;
				}
			}
		}
	}

	public static void montarDadosIdioma(CatalogoVO catalogoVO, DataField field, UsuarioVO usuarioVO) throws Exception {
		if (field != null) {
			LinguaPublicacaoCatalogoVO linguaPublicacaoCatalogoVO = null;
			List<Subfield> subfields = field.getSubfields();
			Iterator<Subfield> i = subfields.iterator();
			while (i.hasNext()) {
				Subfield subfield = i.next();
				String code = String.valueOf(subfield.getCode());
				String data = subfield.getData();
				if (code.equals("a")) {
					linguaPublicacaoCatalogoVO = getFacadeFactory().getLinguaPublicacaoCatalogoFacade().consultarPorMARCCode(data, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
					if (linguaPublicacaoCatalogoVO.getCodigo().equals(0)) {
						linguaPublicacaoCatalogoVO.setNome(CodigoIdiomaEnum.getEnumPorValor(data) == null ? data : CodigoIdiomaEnum.getEnumPorValor(data).getValue());
						linguaPublicacaoCatalogoVO.setMarcCode(data);
					}
				}
			}
			if (linguaPublicacaoCatalogoVO.getCodigo().equals(0)) {
				getFacadeFactory().getLinguaPublicacaoCatalogoFacade().incluir(linguaPublicacaoCatalogoVO, usuarioVO);
			}
			catalogoVO.setLinguaPublicacaoCatalogo(linguaPublicacaoCatalogoVO);
		}
	}

	public static void montarDadosAcessoEletronico(CatalogoVO catalogoVO, DataField field, UsuarioVO usuarioVO) throws Exception {
		if (field != null) {
			List<Subfield> subfields = field.getSubfields();
			Iterator<Subfield> i = subfields.iterator();
			while (i.hasNext()) {
				Subfield subfield = i.next();
				String code = String.valueOf(subfield.getCode());
				String data = subfield.getData();
				if (code.equals("u")) {
					catalogoVO.setLink(data);
					return;
				}
			}
		}
	}
	
	public static void montarDadosAssunto(CatalogoVO catalogoVO, DataField field, UsuarioVO usuarioVO) throws Exception {
		if (field != null) {
			List<Subfield> subfields = field.getSubfields();
			Iterator<Subfield> i = subfields.iterator();
			while (i.hasNext()) {
				Subfield subfield = i.next();
				String code = String.valueOf(subfield.getCode());
				String data = subfield.getData();
				if (code.equals("a")) {
					if (catalogoVO.getAssunto().equals("")) {
						catalogoVO.setAssunto(data);
					} else {
						catalogoVO.setAssunto(catalogoVO.getAssunto() + ", " + data);
					}
				}
			}
		}
	}
	
	public static void montarDadosISBN(CatalogoVO catalogoVO, DataField field, UsuarioVO usuarioVO) throws Exception {
		if (field != null) {
			List<Subfield> subfields = field.getSubfields();
			Iterator<Subfield> i = subfields.iterator();
			while (i.hasNext()) {
				Subfield subfield = i.next();
				String code = String.valueOf(subfield.getCode());
				String data = subfield.getData();
				if (code.equals("a")) {
					catalogoVO.setIsbn(data);
				}
			}
		}
	}
	
	public static void montarDadosDescricaoFisica(CatalogoVO catalogoVO, DataField field, UsuarioVO usuarioVO) throws Exception {
		if (field != null) {
			List<Subfield> subfields = field.getSubfields();
			Iterator<Subfield> i = subfields.iterator();
			while (i.hasNext()) {
				Subfield subfield = i.next();
				String code = String.valueOf(subfield.getCode());
				String data = subfield.getData();
				if (code.equals("a")) {
					String nrPagina = Pattern.compile("[^0-9]").matcher(data).replaceAll("");
					if (!nrPagina.equals("")) {
						catalogoVO.setNrPaginas((Pattern.compile("[^0-9]").matcher(data).replaceAll("")));
					}
				}
			}
		}
	}
	
	public static void montarDadosISSN(CatalogoVO catalogoVO, DataField field, UsuarioVO usuarioVO) throws Exception {
		if (field != null) {
			List<Subfield> subfields = field.getSubfields();
			Iterator<Subfield> i = subfields.iterator();
			while (i.hasNext()) {
				Subfield subfield = i.next();
				String code = String.valueOf(subfield.getCode());
				String data = subfield.getData();
				if (code.equals("a")) {
					catalogoVO.setIssn(data);
				}
			}
		}
	}
	
	public static void montarDadosFormasVariantesTitulo(CatalogoVO catalogoVO, DataField field, UsuarioVO usuarioVO) throws Exception {
		if (field != null) {
			List<Subfield> subfields = field.getSubfields();
			Iterator<Subfield> i = subfields.iterator();
			while (i.hasNext()) {
				Subfield subfield = i.next();
				String code = String.valueOf(subfield.getCode());
				String data = subfield.getData();
				if (code.equals("a")) {
					catalogoVO.setAbreviacaoTitulo(data);
				}
			}
		}
	}
	
	public static void montarDadosLocalizacao(CatalogoVO catalogoVO, DataField field, UsuarioVO usuarioVO) throws Exception {
		if (field != null) {
			List<Subfield> subfields = field.getSubfields();
			Iterator<Subfield> i = subfields.iterator();
			while (i.hasNext()) {
				Subfield subfield = i.next();
				String code = String.valueOf(subfield.getCode());
				String data = subfield.getData();
				if (code.equals("a")) {
					TipoCatalogoVO tipoCatalogoVO = getFacadeFactory().getTipoCatalogoFacade().consultarPorNomeRegistroUnico(data, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
					if (tipoCatalogoVO.getCodigo().equals(0)) {
						tipoCatalogoVO.setNome(data);
						getFacadeFactory().getTipoCatalogoFacade().incluir(tipoCatalogoVO, usuarioVO);
					}
					catalogoVO.setTipoCatalogo(tipoCatalogoVO);
					if (Uteis.removerAcentos(catalogoVO.getTipoCatalogo().getNome()).toUpperCase().equals("PERIODICOS")) {
						catalogoVO.setAssinaturaPeriodico(true);
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ArquivoMarc21VO obj, UsuarioVO usuarioVO) throws Exception {
		if (obj.getArquivoVO().getCodigo().equals(0)) {
			getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoVO(), usuarioVO, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(usuarioVO));
		}
		final String sql = " INSERT INTO arquivomarc21 (responsavel, arquivo) VALUES (?, ?) returning codigo ";
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				sqlInserir.setInt(1, obj.getResponsavel().getCodigo());
				sqlInserir.setInt(2, obj.getArquivoVO().getCodigo());
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
		for (ArquivoMarc21CatalogoVO arquivoMarc21CatalogoVO : obj.getArquivoMarc21CatalogoVOs()) {			
				arquivoMarc21CatalogoVO.setArquivoMarc21VO(obj);
				getFacadeFactory().getArquivoMarc21CatalogoFacade().incluir(arquivoMarc21CatalogoVO, usuarioVO);
			
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ArquivoMarc21VO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			final String sql = "UPDATE arquivomarc21 set responsavel=?, arquivo=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setInt(1, obj.getResponsavel().getCodigo());
					sqlAlterar.setInt(2, obj.getArquivoVO().getCodigo());
					sqlAlterar.setInt(3, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			for (ArquivoMarc21CatalogoVO arquivoMarc21CatalogoVO : obj.getArquivoMarc21CatalogoVOs()) {
				if (arquivoMarc21CatalogoVO.getSelecionado()) {
					arquivoMarc21CatalogoVO.setArquivoMarc21VO(obj);
					getFacadeFactory().getArquivoMarc21CatalogoFacade().incluir(arquivoMarc21CatalogoVO, usuarioVO);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void validarDados(ArquivoMarc21VO obj) throws Exception {
		if (obj.getArquivoVO().getNome().equals("")) {
			throw new Exception(UteisJSF.internacionalizar("msg_ArquivoMarc21_ErroUploadArquivo"));
		}
	}

	public List<ArquivoMarc21VO> consultarPorResponsavel(String valorConsulta, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT arquivomarc21.* FROM arquivomarc21 ");
		sqlStr.append(" INNER JOIN usuario ON usuario.codigo = arquivomarc21.responsavel ");
		sqlStr.append(" WHERE upper(usuario.nome) like '%").append(valorConsulta.toUpperCase()).append("%' ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(rs, nivelMontarDados, usuarioVO);
	}

	public List<ArquivoMarc21VO> consultarPorDataProcessamento(Date dataInicial, Date dataFinal, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT arquivomarc21.* FROM arquivomarc21 ");
		sqlStr.append(" INNER JOIN arquivo ON arquivo.codigo = arquivomarc21.arquivo ");
		sqlStr.append(" WHERE arquivo.dataupload >= '").append(Uteis.getDataBD0000(dataInicial)).append("' ");
		sqlStr.append(" AND arquivo.dataupload <= '").append(Uteis.getDataBD2359(dataFinal)).append("' ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(rs, nivelMontarDados, usuarioVO);
	}

	public static List<ArquivoMarc21VO> montarDadosConsulta(SqlRowSet rs, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<ArquivoMarc21VO> arquivoMarc21VOs = new ArrayList<ArquivoMarc21VO>(0);
		while (rs.next()) {
			arquivoMarc21VOs.add(montarDados(rs, nivelMontarDados, usuario));
		}
		return arquivoMarc21VOs;
	}

	public static ArquivoMarc21VO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ArquivoMarc21VO obj = new ArquivoMarc21VO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.getResponsavel().setCodigo(dadosSQL.getInt("responsavel"));
		obj.getArquivoVO().setCodigo(dadosSQL.getInt("arquivo"));
		montarDadosResponsavel(obj, nivelMontarDados, usuario);
		montarDadosArquivo(obj, nivelMontarDados, usuario);
		return obj;
	}

	public static void montarDadosResponsavel(ArquivoMarc21VO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavel().getCodigo().intValue() == 0) {
			obj.setResponsavel(new UsuarioVO());
			return;
		}
		obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados, usuario));
	}

	public static void montarDadosArquivo(ArquivoMarc21VO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getArquivoVO().getCodigo().intValue() == 0) {
			obj.setArquivoVO(new ArquivoVO());
			return;
		}
		obj.setArquivoVO(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getArquivoVO().getCodigo(), nivelMontarDados, usuario));
	}

	public static String getIdEntidade() {
		return ArquivoMarc21.idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		ArquivoMarc21.idEntidade = idEntidade;
	}
    
	@Override
	public List<ArquivoMarc21VO> montarArquivoMarc21DadosCatalogo(Integer codigoCatalogo ,UsuarioVO usuario) throws  Exception{
		List<ArquivoMarc21VO> listMarcs21 = new ArrayList<ArquivoMarc21VO>(0);		
		 List<ArquivoMarc21CatalogoVO> arquivoMarc21CatalogoVOs =  getFacadeFactory().getCatalogoFacade().consultaRapidaCatalogos(codigoCatalogo ,true, usuario);
		 List<ArquivoMarc21CatalogoVO> lista = new ArrayList<ArquivoMarc21CatalogoVO>(0); 
		 int cont = 0 ;
		  Iterator i = arquivoMarc21CatalogoVOs.iterator();		  
		  while(i.hasNext()){
			  ArquivoMarc21CatalogoVO arquivoMarc21CatalogoVO = (ArquivoMarc21CatalogoVO) i.next();
			  lista.add(arquivoMarc21CatalogoVO);
			  cont ++ ;
			  if(!i.hasNext() && cont < 100) {
				  ArquivoMarc21VO arquivoMarc21 = new ArquivoMarc21VO();
				  arquivoMarc21.setArquivoMarcCatalogoVOs(lista);
				  listMarcs21.add(arquivoMarc21);
			  }
			  if(cont == 100) {
				  ArquivoMarc21VO arquivoMarc21 = new ArquivoMarc21VO();
				  arquivoMarc21.setArquivoMarcCatalogoVOs(lista);
				  listMarcs21.add(arquivoMarc21);
				  cont = 0 ; 
				  lista = new ArrayList<ArquivoMarc21CatalogoVO>();  
			  }
		  }
			
		return listMarcs21;
		
	}
	
	
	
	
	
	
	@Override
	public void executarExportarArquivoMarc21XML(ArquivoMarc21VO arquivoMarc21VO, UsuarioVO usuarioLogado, 
			ConfiguracaoGeralSistemaVO config , String comandoIncluirAlterarExcluir)throws Exception {   
				
			
			ArquivoVO arquivo = new ArquivoVO();			
			arquivo.setPastaBaseArquivo(PastaBaseArquivoEnum.ARQUIVOSBILIOTECAEXTERNA_TMP.getValue());
			arquivo.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.ARQUIVOSBILIOTECAEXTERNA_TMP);
			arquivo.setNome(ArquivoHelper.criarNomeArquivo(usuarioLogado, "xml"));
			arquivo.setDescricao(arquivo.getNome());
			arquivo.setExtensao(arquivo.getNome().substring(arquivo.getDescricao().lastIndexOf("."), arquivo.getNome().length()));	
			arquivoMarc21VO.setArquivoVO(arquivo);
		    OutputStream out = new FileOutputStream(config.getLocalUploadArquivoFixo() + File.separator
		    		    + arquivo.getPastaBaseArquivo() + File.separator
						+ arquivo.getNome());				
			MarcFactory marcFactory = MarcFactory.newInstance();
		    AnselToUnicode converter = new AnselToUnicode();
		    MarcXmlWriter writer = new MarcXmlWriter(out, "UTF-8", true);
			    
		    for (ArquivoMarc21CatalogoVO marc21CatalogoVO : arquivoMarc21VO.getArquivoMarc21CatalogoVOs()) {
		    	 CatalogoVO catalogoVO = marc21CatalogoVO.getCatalogoVO();		    	
		    	 CatalogoVO catalogoVOClone = (CatalogoVO) Uteis.clonar(catalogoVO);		    	
			     catalogoVOClone.setNumeroControle(String.valueOf(catalogoVOClone.getCodigo()));		
				 StringBuilder urlMinhaBibliotecaExterna = new StringBuilder().append(config.getUrlAcessoExternoAplicacao())
							                                      .append(File.separator).append("minhaBiblioteca").append(File.separator)
							                                      .append("homeBibliotecaExterna.xhtml?catalogo=").append(catalogoVOClone.getCodigo());	
			    	
				 catalogoVOClone.setLink(urlMinhaBibliotecaExterna.toString());		    
			    
					Record record = marcFactory.newRecord();
					if(Uteis.isAtributoPreenchido(comandoIncluirAlterarExcluir)) {
						Leader leader = record.getLeader();	
						leader.setRecordStatus(comandoIncluirAlterarExcluir.charAt(0));	
					}										
					record.addVariableField(marcFactory.newControlField("001", catalogoVOClone.getNumeroControle()));
					adicionarFieldTituloPrincipal(catalogoVOClone, marcFactory, record);
					adicionarFieldClassificacao(catalogoVOClone, marcFactory, record);
					adicionarFieldImprenta(catalogoVOClone, marcFactory, record);
					adicionarFieldAutor(catalogoVOClone, marcFactory, record);
					adicionarFieldEdicao(catalogoVOClone, marcFactory, record);
					adicionarFieldIdioma(catalogoVOClone, marcFactory, record);
					adicionarFieldAcessoEletronico(catalogoVOClone, marcFactory, record);
					adicionarFieldAssunto(catalogoVOClone, marcFactory, record);
					adicionarFieldISBN(catalogoVOClone, marcFactory, record);
					adicionarFieldDescricaoFisica(catalogoVOClone, marcFactory, record);
					adicionarFieldISSN(catalogoVOClone, marcFactory, record);
					adicionarFieldFormasVariantesTitulo(catalogoVOClone, marcFactory, record);
					adicionarFieldLocalizacao(catalogoVOClone, marcFactory, record);
					//writer.setConverter(converter);
					writer.setUnicodeNormalization(true);
				    writer.write(record);				
				} 	
		    writer.close();
	}

}