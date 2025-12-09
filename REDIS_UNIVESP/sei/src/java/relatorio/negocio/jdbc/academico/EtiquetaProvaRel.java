package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.LayoutEtiquetaTagVO;
import negocio.comuns.basico.LayoutEtiquetaVO;
import negocio.comuns.basico.enumeradores.TagEtiquetaEnum;
import negocio.comuns.biblioteca.enumeradores.TipoRelatorioEtiquetaAcademicoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import relatorio.negocio.comuns.academico.EtiquetaProvaRelVO;
import relatorio.negocio.interfaces.academico.EtiquetaProvaRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

/**
 *
 * @author Carlos
 */
@Repository
@Scope("singleton")
@Lazy
public class EtiquetaProvaRel extends SuperRelatorio implements EtiquetaProvaRelInterfaceFacade {

	public static final float PONTO = 2.83f;
	
    public EtiquetaProvaRel() {
    }

    public void validarDados(Integer unidadeEnsino, TurmaVO turma, String ano, String semestre) throws Exception {
        if (unidadeEnsino == null || unidadeEnsino == 0) {
            throw new Exception("O campo UNIDADE DE ENSINO deve ser informado.");
        }
        if (turma == null || turma.getCodigo() == 0) {
            throw new Exception("O campo IDENTIFICADOR TURMA deve ser informado.");
        }
        if (turma.getCurso().getPeriodicidade().equals("AN")) {
        	if (ano.equals("")) {
        		throw new Exception("O campo ANO deve ser informado.");
        	}
        }
        if (turma.getCurso().getPeriodicidade().equals("SE")) {
            if (ano.equals("")) {
                throw new Exception("O campo ANO deve ser informado.");
            }
            if (semestre.equals("")) {
                throw new Exception("O campo SEMESTRE deve ser informado.");
            }
        }
    }
    
    @Override
    public String realizarImpressaoEtiquetaProva(LayoutEtiquetaVO layoutEtiqueta, List<EtiquetaProvaRelVO> etiquetaProvaRelVOs, Integer numeroCopias, Integer linha, Integer coluna, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception {
    	Rectangle tamanhoPagina = null;
		Document pdf = null;
		FileOutputStream arquivo = null;
		String nomeArquivo = String.valueOf(new Date().getTime()) + ".pdf";
		StringBuffer caminhoDaImagem = new StringBuffer();
		try {
			if (layoutEtiqueta.getAlturaEtiqueta().equals(0) || layoutEtiqueta.getAlturaFolhaImpressao().equals(0) || layoutEtiqueta.getLarguraEtiqueta().equals(0) || layoutEtiqueta.getLarguraFolhaImpressao().equals(0)) {
				throw new Exception("Insira um valor para altura ou largura");
			}
			layoutEtiqueta.setLayoutEtiquetaTagVO(getFacadeFactory().getLayoutEtiquetaTagFacade().consultarLayoutEtiquetaTagItens(layoutEtiqueta.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, configuracaoGeralSistemaVO, usuarioLogado));
			float alturaPagina = layoutEtiqueta.getAlturaFolhaImpressao() * PONTO;
			float larguraPagina = layoutEtiqueta.getLarguraFolhaImpressao() * PONTO;
			float margemEsquerda = layoutEtiqueta.getMargemEsquerdaEtiquetaFolha().floatValue() * PONTO;
			float margemDireita = layoutEtiqueta.getMargemEsquerdaEtiquetaFolha().floatValue() * PONTO;
			float margemSuperior = layoutEtiqueta.getMargemSuperiorEtiquetaFolha().floatValue() * PONTO;
			float margemInferior = layoutEtiqueta.getMargemSuperiorEtiquetaFolha().floatValue() * PONTO;
			
			caminhoDaImagem.append(UteisJSF.getCaminhoWeb() + File.separator + "relatorio" + File.separator + nomeArquivo);
			tamanhoPagina = new Rectangle(larguraPagina, alturaPagina);
			pdf = new Document(tamanhoPagina, margemEsquerda, margemDireita, margemSuperior, margemInferior);
			arquivo = new FileOutputStream(caminhoDaImagem.toString());
			PdfWriter writer = PdfWriter.getInstance(pdf, arquivo);
			pdf.open();
			
			realizarMontagemPreviewEtiquetLaserTinta(layoutEtiqueta, writer, pdf, etiquetaProvaRelVOs, numeroCopias, linha, coluna);
			return nomeArquivo;
		} catch (Exception e) {
			throw e;
		} finally {
			if (pdf != null) {
				pdf.close();
			}
			if (arquivo != null) {
				arquivo.close();
			}
		}
    }
    
    public void realizarMontagemPreviewEtiquetLaserTinta(LayoutEtiquetaVO layoutEtiqueta, PdfWriter writer, Document pdf, List<EtiquetaProvaRelVO> etiquetaProvaRelVOs, Integer numeroCopias, Integer linha, Integer coluna) throws Exception {
    	float alturaPagina = layoutEtiqueta.getAlturaFolhaImpressao() * PONTO;

		float margemEsquerda = layoutEtiqueta.getMargemEsquerdaEtiquetaFolha().floatValue() * PONTO;

		float margemSuperior = layoutEtiqueta.getMargemSuperiorEtiquetaFolha().floatValue() * PONTO;

		float alturaEtiqueta = layoutEtiqueta.getAlturaEtiqueta().floatValue() * PONTO;
		float larguraEtiqueta = layoutEtiqueta.getLarguraEtiqueta().floatValue() * PONTO;
		float margemHorizontalEntreEtiquetas = (layoutEtiqueta.getMargemEntreEtiquetaHorizontal() == 0 ? 0.2f : layoutEtiqueta.getMargemEntreEtiquetaHorizontal().floatValue()) * PONTO;
		float margemVerticalEntreEtiquetas = layoutEtiqueta.getMargemEntreEtiquetaVertical() * PONTO;
		Float margemSuperiorColuna = 0f;
		Float margemEsquerdaLabel = 0f;
		Float margemSuperiorLabel = 0f;
		Float margemEsquerdaColuna = 0f;

		PdfContentByte canvas = writer.getDirectContent();

		Integer linhas = layoutEtiqueta.getNumeroLinhasEtiqueta();
		Integer colunas = layoutEtiqueta.getNumeroColunasEtiqueta();
		
		String professor = etiquetaProvaRelVOs.get(0).getProfessor();

		for (EtiquetaProvaRelVO etiquetaProvaRelVO : etiquetaProvaRelVOs) {
			etiquetaProvaRelVO.setProfessor(professor);
			for (int copia = 1; copia <= numeroCopias; copia++) {
				margemSuperiorColuna = alturaPagina - margemSuperior - ((linha - 1) * margemHorizontalEntreEtiquetas) - ((linha - 1) * alturaEtiqueta);
				margemEsquerdaColuna = margemEsquerda + ((coluna - 1) * margemVerticalEntreEtiquetas) + ((coluna - 1) * larguraEtiqueta);

				for (LayoutEtiquetaTagVO tag : layoutEtiqueta.getLayoutEtiquetaTagVOs()) {
					margemEsquerdaLabel = tag.getMargemDireita() * PONTO;
					margemSuperiorLabel = tag.getMargemTopo() * PONTO;
					PdfTemplate tmp = canvas.createTemplate(larguraEtiqueta, alturaEtiqueta);
					tmp.beginText();
					tmp.setFontAndSize(BaseFont.createFont(FontFactory.TIMES_ROMAN, "", true), tag.getTamanhoFonte().floatValue());
//					tmp.showTextAligned(Element.ALIGN_LEFT, (tag.getLabelTag() + " " + getValorImprimirEtiqueta(etiquetaProvaRelVO, tag.getTagEtiqueta())).trim(), margemEsquerdaLabel.floatValue(), alturaEtiqueta - margemSuperiorLabel, tag.getRotacao() ? 90f : 0f);
					tmp.showTextAligned(Element.ALIGN_LEFT, (getValorImprimirEtiqueta(etiquetaProvaRelVO, tag.getTagEtiqueta(), tag.getLabelTag(), tag.getApresentarLabelEtiquetaAposTagEtiqueta())).trim(), margemEsquerdaLabel.floatValue(), alturaEtiqueta - margemSuperiorLabel, tag.getRotacao() ? 90f : 0f);
					
					tmp.endText();
					canvas.addTemplate(tmp, margemEsquerdaColuna, margemSuperiorColuna - alturaEtiqueta);

				}
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
			}
		}
    }
    
    private String getValorImprimirEtiqueta(EtiquetaProvaRelVO etiquetaProvaRelVO, TagEtiquetaEnum tag, String label, Boolean apresentarLabelEtiquetaAposTagEtiqueta) {
		switch (tag) {
		case NOME_PESSOA:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return Uteis.getNomeResumidoPessoa(etiquetaProvaRelVO.getNome()).trim().isEmpty() ? ""
						: Uteis.getNomeResumidoPessoa(etiquetaProvaRelVO.getNome()).trim() + " " + label;
			} else {
				return Uteis.getNomeResumidoPessoa(etiquetaProvaRelVO.getNome()).trim().isEmpty() ? ""
						: label + " " + Uteis.getNomeResumidoPessoa(etiquetaProvaRelVO.getNome()).trim();
			}
		case MATRICULA:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaProvaRelVO.getMatricula().trim().isEmpty() ? ""
						: etiquetaProvaRelVO.getMatricula().trim() + " " + label;
			} else {
				return etiquetaProvaRelVO.getMatricula().trim().isEmpty() ? ""
						: label + " " + etiquetaProvaRelVO.getMatricula().trim();
			}
		case ANO:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaProvaRelVO.getMatriculaPeriodoVO().getAno().trim().isEmpty() ? ""
						: etiquetaProvaRelVO.getMatriculaPeriodoVO().getAno().trim() + " " + label;
			} else {
				return etiquetaProvaRelVO.getMatriculaPeriodoVO().getAno().trim().isEmpty() ? ""
						: label + " " + etiquetaProvaRelVO.getMatriculaPeriodoVO().getAno().trim();
			}
		case SEMESTRE:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaProvaRelVO.getMatriculaPeriodoVO().getSemestre().trim().isEmpty() ? ""
						: etiquetaProvaRelVO.getMatriculaPeriodoVO().getSemestre().trim() + " " + label;
			} else {
				return etiquetaProvaRelVO.getMatriculaPeriodoVO().getSemestre().trim().isEmpty() ? ""
						: label + " " + etiquetaProvaRelVO.getMatriculaPeriodoVO().getSemestre().trim();
			}
		case DISCIPLINA:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaProvaRelVO.getDisciplina().trim().isEmpty() ? ""
						: etiquetaProvaRelVO.getDisciplina().trim() + " " + label;
			} else {
				return etiquetaProvaRelVO.getDisciplina().trim().isEmpty() ? ""
						: label + " " + etiquetaProvaRelVO.getDisciplina().trim();
			}
		case TURMA:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaProvaRelVO.getTurma().trim().isEmpty() ? ""
						: etiquetaProvaRelVO.getTurma().trim() + " " + label;
			} else {
				return etiquetaProvaRelVO.getTurma().trim().isEmpty() ? ""
						: label + " " + etiquetaProvaRelVO.getTurma().trim();
			}
		case PERIODO_LETIVO:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaProvaRelVO.getMatriculaPeriodoVO().getPeriodoLetivo().getDescricao().trim().isEmpty() ? ""
						: etiquetaProvaRelVO.getMatriculaPeriodoVO().getPeriodoLetivo().getDescricao().trim() + " " + label;
			} else {
				return etiquetaProvaRelVO.getMatriculaPeriodoVO().getPeriodoLetivo().getDescricao().trim().isEmpty() ? ""
						: label + " " + etiquetaProvaRelVO.getMatriculaPeriodoVO().getPeriodoLetivo().getDescricao().trim();
			}
		case TURNO:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaProvaRelVO.getTurno().trim().isEmpty() ? ""
						: etiquetaProvaRelVO.getTurno().trim() + " " + label;
			} else {
				return etiquetaProvaRelVO.getTurno().trim().isEmpty() ? ""
						: label + " " + etiquetaProvaRelVO.getTurno().trim();
			}
		case NOME_CURSO:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getCurso().getNome().trim().isEmpty() ? ""
						: etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getCurso().getNome().trim() + " " + label;
			} else {
				return etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getCurso().getNome().trim().isEmpty() ? ""
						: label + " " + etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getCurso().getNome().trim();
			}
		case PROFESSOR:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return Uteis.getNomeResumidoPessoa(etiquetaProvaRelVO.getProfessor()).trim().isEmpty() ? ""
						: Uteis.getNomeResumidoPessoa(etiquetaProvaRelVO.getProfessor()).trim() + " " + label;
			} else {
				return Uteis.getNomeResumidoPessoa(etiquetaProvaRelVO.getProfessor()).trim().isEmpty() ? ""
						: label + " " + Uteis.getNomeResumidoPessoa(etiquetaProvaRelVO.getProfessor()).trim();
			}
		case CIDADE_UNIDADE_ENSINO:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCidade()
						.getNome().toUpperCase().trim().isEmpty() ? ""
								: etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino()
										.getCidade().getNome().toUpperCase().trim() + " " + label;
			} else {
				return etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCidade()
						.getNome().toUpperCase().trim().isEmpty() ? ""
								: label + " " + etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO()
										.getUnidadeEnsino().getCidade().getNome().toUpperCase().trim();
			}
		case ESTADO_UNIDADE_ENSINO:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCidade()
						.getEstado().getSigla().toUpperCase().trim().isEmpty() ? ""
								: etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino()
										.getCidade().getEstado().getSigla().toUpperCase().trim() + " " + label;
			} else {
				return etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCidade()
						.getEstado().getSigla().toUpperCase().trim().isEmpty() ? ""
								: label + " " + etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO()
										.getUnidadeEnsino().getCidade().getEstado().getSigla().toUpperCase().trim();
			}
		case CPF_PESSOA:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getCPF().isEmpty() ? ""
						: etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getCPF() + " " + label;
			} else {
				return etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getCPF().isEmpty() ? ""
						: label + " " + etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getCPF();
			}
		case RG_PESSOA:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getRG().isEmpty() ? ""
						: etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getRG() + " " + label;
			} else {
				return etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getRG().isEmpty() ? ""
						: label + " " + etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getRG();
			}
		case ENDERECO:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getEndereco().trim().isEmpty() ? "" : etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getEndereco().trim()+" "+label;
			} else {
				return etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getEndereco().trim().isEmpty() ? "" : label+" "+etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getEndereco().trim();
			}
		case BAIRRO:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getSetor().trim().isEmpty() ? "" : etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getSetor().trim()+" "+label;
			} else {
				return etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getSetor().trim().isEmpty() ? "" : label+" "+etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getSetor().trim();
			}
		case CEP:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getCEP().trim().isEmpty() ? "" : etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getCEP().trim() + " "+label;
			} else {
				return etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getCEP().trim().isEmpty() ? "" : label+" "+etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getCEP().trim();
			}
		case COMPLEMENTO:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getComplemento().trim().isEmpty() ? "" : etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getComplemento().trim()+" "+label;
			} else {
				return etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getComplemento().trim().isEmpty() ? "" : label+" "+etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getComplemento().trim();
			}
		case TELEFONE:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getTelefoneRes().trim().isEmpty()?"":etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getTelefoneRes().trim()+" "+label;
			} else {
				return etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getTelefoneRes().trim().isEmpty()?"":label+" "+etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getTelefoneRes().trim();
			}
		case CELULAR:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getCelular().trim().isEmpty() ? "" : etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getCelular().trim()+" "+label;
			} else {
				return etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getCelular().trim().isEmpty() ? "" : label+" "+etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getCelular().trim();
			}
		case CIDADE:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getCidade().getNome().trim().isEmpty() ? "" : etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getCidade().getNome().trim() +" "+label;
			} else {
				return etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getCidade().getNome().trim().isEmpty() ? "" : label+" "+etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getCidade().getNome().trim();
			}
		case ESTADO:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getCidade().getEstado().getSigla().trim().isEmpty() ? "" : etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getCidade().getEstado().getSigla().trim()+" "+label ;
			} else {
				return etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getCidade().getEstado().getSigla().trim().isEmpty() ? "" : label+" "+etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getCidade().getEstado().getSigla().trim();			
			}
		case NUMERO:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getNumero().trim().isEmpty() ? "" : etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getNumero().trim()+" "+label;
			} else {
				return etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getNumero().trim().isEmpty() ? "" : label+" "+etiquetaProvaRelVO.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getNumero().trim();
			}
		default:
			return "";
		}
	}

    public void validarDadosPreenchimentoLista(List<EtiquetaProvaRelVO> vetResultado) throws Exception {
        if (vetResultado.isEmpty()) {
            throw new Exception("Não há dados a serem exibidos no relatório.");
        }
    }

    public EtiquetaProvaRelVO realizarCriacaoOjbRel(Integer unidadeEnsino, TurmaVO turma, Integer disciplina, String ano, String semestre, String turno, Boolean trazerAlunoPendenteFinanceiramente, TipoRelatorioEtiquetaAcademicoEnum tipoRelatorioAcademico, FiltroRelatorioAcademicoVO filtroAcademicoVO) throws Exception {
        EtiquetaProvaRelVO etiquetaProvaRelVO = new EtiquetaProvaRelVO();
        List<EtiquetaProvaRelVO> vetResultado = consultarOjbRel(unidadeEnsino, turma, disciplina, ano, semestre, turno, trazerAlunoPendenteFinanceiramente, tipoRelatorioAcademico, filtroAcademicoVO);
        validarDadosPreenchimentoLista(vetResultado);
        executarDivisaoColunas(vetResultado, etiquetaProvaRelVO);
        
        return etiquetaProvaRelVO;
    }

    public void executarDivisaoColunas(List<EtiquetaProvaRelVO> vetResultado, EtiquetaProvaRelVO etiquetaProvaRelVO) {
        int col = 1;
        for (EtiquetaProvaRelVO etiqueta : vetResultado) {
            if (col == 1) {
                etiquetaProvaRelVO.getListaEtiquetaProvaColuna1().add(etiqueta);
                col = 2;
                continue;
            }
            if (col == 2) {
                etiquetaProvaRelVO.getListaEtiquetaProvaColuna2().add(etiqueta);
                col = 3;
                continue;
            }
            if (col == 3) {
                etiquetaProvaRelVO.getListaEtiquetaProvaColuna3().add(etiqueta);
                col = 1;
                continue;
            }
        }
    }
    
    @Override
    public List<EtiquetaProvaRelVO> consultarOjbRel(Integer unidadeEnsino, TurmaVO turma, Integer disciplina, String ano, String semestre, String turno, Boolean trazerAlunoPendenteFinanceiramente, TipoRelatorioEtiquetaAcademicoEnum tipoRelatorioAcademico, FiltroRelatorioAcademicoVO filtroAcademicoVO) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT DISTINCT pessoa.nome AS \"pessoa.nome\", pessoa.endereco, pessoa.numero, pessoa.setor, matricula.matricula, turma.identificadorturma, ");
        sb.append(" pessoa.complemento, pessoa.telefoneRes, pessoa.celular, pessoa.cpf, periodoletivo.descricao as periodoletivo,  pessoa.rg, pessoa.cep,  ");
		sb.append(" cidade.nome as \"pessoa.cidade\", estado.sigla as \"pessoa.estado\", ");
		sb.append(" cidadeunidade.nome AS cidadeunidade, estadounidade.sigla AS estadounidade, curso.nome AS \"curso.nome\", unidadeensino.nome AS \"unidadeensino.nome\", ");
        if (disciplina != null && disciplina != 0) {
            sb.append(" disciplina.nome AS \"disciplina.nome\", matriculaperiodo.ano, turno.nome AS \"turno.nome\", matriculaperiodo.semestre ");
        } else {
            sb.append(" matriculaperiodo.ano, turno.nome AS \"turno.nome\", matriculaperiodo.semestre ");
        }
        sb.append(" FROM turma ");
        sb.append(" INNER JOIN matriculaPeriodoTurmaDisciplina mptd ON mptd.turma = turma.codigo ");
        sb.append(" INNER JOIN matriculaPeriodo ON matriculaPeriodo.codigo = mptd.matriculaPeriodo ");
        sb.append(" inner join periodoletivo on periodoletivo.codigo = matriculaperiodo.periodoletivomatricula ");
        //sb.append(" INNER JOIN turmaDisciplina ON turmadisciplina.turma = turma.codigo ");
        sb.append(" INNER JOIN disciplina ON disciplina.codigo = mptd.disciplina ");
        sb.append(" INNER JOIN matricula ON matricula.matricula = matriculaperiodo.matricula ");
        sb.append(" INNER JOIN pessoa ON pessoa.codigo = matricula.aluno ");
        sb.append(" left JOIN cidade ON pessoa.cidade = cidade.codigo ");
        sb.append(" left JOIN estado ON estado.codigo = cidade.estado ");
        sb.append(" INNER JOIN turno ON turno.codigo = matricula.turno ");
        sb.append(" INNER JOIN unidadeensino ON unidadeensino.codigo = matricula.unidadeensino ");
        sb.append(" left JOIN cidade as cidadeunidade ON cidadeunidade.codigo = unidadeensino.cidade ");
        sb.append(" left JOIN estado as estadounidade ON cidadeunidade.estado = estadounidade.codigo ");
        sb.append(" INNER JOIN curso ON curso.codigo = matricula.curso ");
        if (Uteis.isAtributoPreenchido(turma)) {
        	if (turma.getTurmaAgrupada() && !turma.getSubturma()) {
        		sb.append(" WHERE  ((turma.codigo = ").append(turma.getCodigo()).append(" or turma.codigo in (select turma from turmaAgrupada where turmaOrigem =  ").append(turma.getCodigo()).append("))");
        		sb.append("or (MatriculaPeriodoTurmaDisciplina.turmaPratica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turma.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'PRATICA'))");
        		sb.append("or (MatriculaPeriodoTurmaDisciplina.turmaTeorica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turma.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'TEORICA'))");
        		sb.append(") ");
        	} else {
        		sb.append(" WHERE Turma.codigo = ").append(turma.getCodigo());
        	}
		}else {
			sb.append(" WHERE matricula.unidadeEnsino = ");
			sb.append(unidadeEnsino.intValue());
		}
        if (disciplina != null && disciplina != 0) {
            sb.append(" AND disciplina.codigo = ");
            sb.append(disciplina.intValue());
        }
        if (!ano.equals("")) {
            sb.append(" AND matriculaperiodo.ano = '");
            sb.append(ano);
            sb.append("'");
        }
        if (!semestre.equals("")) {
            sb.append(" AND matriculaperiodo.semestre = '");
            sb.append(semestre);
            sb.append("'");
        }
//        if (trazerAlunoPendenteFinanceiramente) {
//            sb.append(" AND (matriculaPeriodo.situacaoMatriculaPeriodo = 'AT' OR matriculaPeriodo.situacaoMatriculaPeriodo = 'PF') ");
//        } else {
//            sb.append(" AND matriculaPeriodo.situacaoMatriculaPeriodo = 'AT' ");
//        }
        
//        sb.append(" AND matricula.situacao = 'AT' ");
        sb.append(" AND ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroAcademicoVO, "matriculaperiodo"));
        sb.append(" AND ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroAcademicoVO, "matriculaperiodo"));        
        if (disciplina != null && disciplina != 0) {
            sb.append(" ORDER BY pessoa.nome, disciplina.nome");
        } else {
            sb.append(" ORDER BY pessoa.nome");
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        return montarDadosConsulta(tabelaResultado, disciplina, tipoRelatorioAcademico);

    }
    
//    public void adicionarFiltroRelatorioAcademico(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, StringBuilder sqlStr) {
//      	 sqlStr.append(" AND matriculaperiodo.situacaomatriculaperiodo in (''");
//      
//      	if (filtroRelatorioAcademicoVO.getAtivo()) {
//              sqlStr.append(", 'AT'");
//       }
//      	if (filtroRelatorioAcademicoVO.getPreMatricula()) {
//      		sqlStr.append(", 'PR'");
//      	}
//      	if (filtroRelatorioAcademicoVO.getPreMatriculaCancelada()) {
//      		sqlStr.append(", 'PC'");
//      	}
//      	if (filtroRelatorioAcademicoVO.getTrancado()) {
//      		sqlStr.append(", 'TR'");
//      	}
//      	if (filtroRelatorioAcademicoVO.getConcluido()) {
//      		sqlStr.append(", 'FI'");
//      	}
//      	if (filtroRelatorioAcademicoVO.getCancelado()) {
//      		sqlStr.append(", 'CA'");
//      	}
//      	sqlStr.append(") ");
//      	if (!filtroRelatorioAcademicoVO.getPendenteFinanceiro()) {
//      		sqlStr.append(" AND matriculaperiodo.situacao != 'PF'");
//      	}
//      }

    public static List<EtiquetaProvaRelVO> montarDadosConsulta(SqlRowSet tabelaResultado, Integer disciplina, TipoRelatorioEtiquetaAcademicoEnum tipoRelatorioAcademico) throws Exception {
        List<EtiquetaProvaRelVO> vetResultado = new ArrayList<EtiquetaProvaRelVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, disciplina, tipoRelatorioAcademico));
        }
        return vetResultado;
    }

    public static EtiquetaProvaRelVO montarDados(SqlRowSet dadosSql, Integer disciplina, TipoRelatorioEtiquetaAcademicoEnum tipoRelatorioAcademico) throws Exception {
        EtiquetaProvaRelVO obj = new EtiquetaProvaRelVO();
        obj.setMatricula(dadosSql.getString("matricula"));
        obj.setNome(dadosSql.getString("pessoa.nome"));
        obj.setTurma(dadosSql.getString("identificadorturma"));
        obj.setTurno(dadosSql.getString("turno.nome"));        
        obj.getMatriculaPeriodoVO().setAno(dadosSql.getString("ano"));
        obj.getMatriculaPeriodoVO().setSemestre(dadosSql.getString("semestre"));
        obj.getMatriculaPeriodoVO().getMatriculaVO().getAluno().setEndereco(dadosSql.getString("endereco"));
		obj.getMatriculaPeriodoVO().getMatriculaVO().getAluno().setNumero(dadosSql.getString("numero"));
		obj.getMatriculaPeriodoVO().getMatriculaVO().getAluno().setSetor(dadosSql.getString("setor"));
		obj.getMatriculaPeriodoVO().getMatriculaVO().getAluno().setComplemento(dadosSql.getString("complemento"));
		obj.getMatriculaPeriodoVO().getMatriculaVO().getAluno().setTelefoneRes(dadosSql.getString("telefoneRes"));
		obj.getMatriculaPeriodoVO().getMatriculaVO().getAluno().setCelular(dadosSql.getString("celular"));
		obj.getMatriculaPeriodoVO().getMatriculaVO().getAluno().setCPF(dadosSql.getString("cpf"));
		obj.getMatriculaPeriodoVO().getMatriculaVO().getAluno().setRG(dadosSql.getString("rg"));
		obj.getMatriculaPeriodoVO().getMatriculaVO().getAluno().setCEP(dadosSql.getString("cep"));
		obj.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getCidade().setNome(dadosSql.getString("pessoa.cidade"));
		obj.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getCidade().getEstado().setSigla(dadosSql.getString("pessoa.estado"));
		obj.getMatriculaPeriodoVO().getPeridoLetivo().setDescricao(dadosSql.getString("periodoletivo"));
        obj.getMatriculaPeriodoVO().getMatriculaVO().getCurso().setNome("curso.nome");
        obj.getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().setNome("unidadeensino.nome");
        obj.getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCidade().setNome("cidadeunidade");
        obj.getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCidade().getEstado().setSigla("estadounidade");
        if(tipoRelatorioAcademico == TipoRelatorioEtiquetaAcademicoEnum.ETIQUETA_PROVA){
            obj.setTipoRelatorio("prova");
        }else{
            obj.setTipoRelatorio("pasta");
        }
        if (disciplina == null || disciplina == 0) {
            obj.setDisciplina("");
        } else {
            obj.setDisciplina(dadosSql.getString("disciplina.nome"));
        }
        if ((dadosSql.getString("ano") == null || dadosSql.getString("ano").equals(""))
                && (dadosSql.getString("semestre") == null || dadosSql.getString("semestre").equals(""))) {
            obj.setPeriodo("");
        } else {
            obj.setPeriodo(dadosSql.getString("ano") + " / " + dadosSql.getString("semestre"));
        }
        return obj;
    }

    public List montarListaSelectItemUnidadeEnsino(UnidadeEnsinoVO unidadeEnsinoLogado, UsuarioVO usuarioLogado) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        resultadoConsulta = consultarUnidadeEnsinoPorNome("", unidadeEnsinoLogado, usuarioLogado);
        i = resultadoConsulta.iterator();
        List objs = new ArrayList(0);
        if (unidadeEnsinoLogado.getCodigo().equals(0)) {
            objs.add(new SelectItem(0, ""));
        }
        while (i.hasNext()) {
            UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
            objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
        }
        return objs;
    }

    public List consultarUnidadeEnsinoPorNome(String nomePrm, UnidadeEnsinoVO unidadeEnsinoLogado, UsuarioVO usuarioLogado) throws Exception {
        List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, unidadeEnsinoLogado.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
        return lista;
    }

    public List consultarTurma(String campoConsultaTurma, UnidadeEnsinoVO unidadeEnsinoVO, String valorConsultaTurma, UsuarioVO usuarioLogado) throws Exception {
        if (campoConsultaTurma.equals("identificadorTurma")) {
            return getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(valorConsultaTurma, unidadeEnsinoVO.getCodigo(), false, false, "", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioLogado);
        }
        if (campoConsultaTurma.equals("nomeTurno")) {
            return getFacadeFactory().getTurmaFacade().consultaRapidaPorTurno(valorConsultaTurma, unidadeEnsinoVO.getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioLogado);
        }
        if (campoConsultaTurma.equals("nomeCurso")) {
            return getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(valorConsultaTurma, unidadeEnsinoVO.getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioLogado);
        }
        return new ArrayList(0);
    }

    public static String getDesignIReportRelatorio(TipoRelatorioEtiquetaAcademicoEnum tipoRelatorio) {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
       
    }

    public static String getCaminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
    }

    public static String getIdEntidade() {
        return ("EtiquetaProvaRel");
    }

    public static String getIdEntidadePasta() {
        return ("EtiquetaPastaRel");
    }
}
