package negocio.comuns.patrimonio;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.patrimonio.enumeradores.TipoUsoTextoPadraoPatrimonioEnum;
import negocio.comuns.utilitarias.dominios.OrientacaoPaginaEnum;

public class TextoPadraoPatrimonioVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -162743908865485665L;

	private Integer codigo;
	private String nome;
	private String textoPadrao;
	private String textoTopo;
	private String textoRodape;
	private Float alturaTopo;
	private Float alturaRodape;
	private StatusAtivoInativoEnum situacao;
	private OrientacaoPaginaEnum orientacaoDaPagina;
	private TipoUsoTextoPadraoPatrimonioEnum tipoUso;
	private Float margemDireita;
	private Float margemEsquerda;
	private Float margemSuperior;
	private Float margemInferior;
	
	/**
	 * Transiente
	 */	
	private String textoPadraoImpressao;
	private String textoPadraoCabecalho;
	private String textoPadraoRodape;
	private String textoPadraoCorpo;
	
	public TextoPadraoPatrimonioVO() {
//		setTextoPadrao(iniciarDeclaracaoPadrao());
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTextoPadrao() {
		if (textoPadrao == null) {
			textoPadrao = "";
		}
		return textoPadrao;
	}

	public void setTextoPadrao(String textoPadrao) {
		this.textoPadrao = textoPadrao;
	}

	public StatusAtivoInativoEnum getSituacao() {
		if (situacao == null) {
			situacao = StatusAtivoInativoEnum.ATIVO;
		}
		return situacao;
	}

	public void setSituacao(StatusAtivoInativoEnum situacao) {
		this.situacao = situacao;
	}

	public OrientacaoPaginaEnum getOrientacaoDaPagina() {
		if (orientacaoDaPagina == null) {
			orientacaoDaPagina = OrientacaoPaginaEnum.RETRATO;
		}
		return orientacaoDaPagina;
	}

	public void setOrientacaoDaPagina(OrientacaoPaginaEnum orientacaoDaPagina) {
		this.orientacaoDaPagina = orientacaoDaPagina;
	}

	public Float getMargemDireita() {
		if (margemDireita == null) {
			margemDireita = 1.0f;
		}
		return margemDireita;
	}

	public void setMargemDireita(Float margemDireita) {
		this.margemDireita = margemDireita;
	}

	public Float getMargemEsquerda() {
		if (margemEsquerda == null) {
			margemEsquerda = 1.0f;
		}
		return margemEsquerda;
	}

	public void setMargemEsquerda(Float margemEsquerda) {
		this.margemEsquerda = margemEsquerda;
	}

	public Float getMargemSuperior() {
		if (margemSuperior == null) {
			margemSuperior = 1.0f;
		}
		return margemSuperior;
	}

	public void setMargemSuperior(Float margemSuperior) {
		this.margemSuperior = margemSuperior;
	}

	public Float getMargemInferior() {
		if (margemInferior == null) {
			margemInferior = 1.0f;
		}
		return margemInferior;
	}

	public void setMargemInferior(Float margemInferior) {
		this.margemInferior = margemInferior;
	}
	
	public String iniciarDeclaracaoPadrao() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<style type='text/css'>");
        sb.append(" body { margin: 0; padding: 0; font-size:11px; } ");
        sb.append(" th { font-weight: normal; } ");
        sb.append(" * { box-sizing: border-box; -moz-box-sizing: border-box; } ");
        sb.append(" .page { width: 21cm; min-height: 29.7cm; padding: 2cm; margin: 1cm auto; } ");
        sb.append(" .subpage { padding-top: 1cm; padding-bottom: 1cm; padding-left: 1cm; padding-right: 1cm; height: 237mm; } ");
        sb.append(" @page { size: A4; margin: 0; } ");
        sb.append(" @media print { .page { margin: 0; border: initial; border-radius: initial; width: initial; min-height: initial; box-shadow: initial; background: initial; page-break-after: always; } } ");
        sb.append("</style>");
        sb.append("</head>");
        sb.append("<body>");
        sb.append("<div class='page' style='padding: 2cm; width: 21cm; margin: 1cm auto; min-height: 29.7cm;'>");
        sb.append("<div class='subpage' style='border: 1px #CCCCCC dashed; height: 237mm; padding: 1cm;'>");
        sb.append("<div><span style='display:none'>&nbsp;</span>&nbsp;</div>");
        sb.append("<p>&nbsp;</p>");
        sb.append("<p>&nbsp;</p>");
        sb.append("<p>&nbsp;</p>");
        sb.append("<p style=\"text-align: center;\"><span style=\"font-size: large;\">EXEMPLO DECLARA&Ccedil;&Atilde;O</span></p>");
        sb.append("<p>&nbsp;</p>");
        sb.append("<p style=\"text-align: center;\"><span style=\"white-space: pre;\"> </span>Declaramos, para os devidos fins, que [(70){}Nome_Aluno],&nbsp;portador(a)&nbsp;da Carteira de Identidade [(20){}Rg_Aluno]&nbsp;e&nbsp;</p>");
        sb.append("<p style=\"text-align: center;\">CPF.: [(14){}Cpf_Aluno],&nbsp;Matr&iacute;cula n&ordm; [(20){}Matricula_Matricula], &eacute; aluno(a)&nbsp;desta Institui&ccedil;&atilde;o de Ensino,&nbsp;matriculado(a) no Curso de&nbsp;</p>");
        sb.append("<p style=\"text-align: center;\">[(70){}Nome_Curso],&nbsp;e vem frequentando as aulas normalmente.</p>");
        sb.append("<p style=\"text-align: center;\">Por ser verdade, firmamos a presente.</p>");
        sb.append("<p style=\"text-align: center;\">&nbsp;</p>");
        sb.append("<p style=\"text-align: center;\">&nbsp;</p>");
        sb.append("<p style=\"text-align: center;\">[(10){}DataAtual_Outras]");
        sb.append("<p style=\"text-align: center;\">&nbsp;</p>");
        sb.append("<p>&nbsp;</p>");
        sb.append("<p style=\"text-align: center;\">______________________ &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;_________________________</p>");
        sb.append("<p style=\"text-align: center;\">Diretor(a) &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; Secret&aacute;ria Geral</p>");
        sb.append("<p>&nbsp;</p>");
        sb.append("<p>&nbsp;</p>");
        sb.append("</div>");
        sb.append("</div>");
        sb.append("</body>");
        sb.append("</html>");
        return sb.toString();
    }

	/**
	 * @return the tipoUso
	 */
	public TipoUsoTextoPadraoPatrimonioEnum getTipoUso() {
		if (tipoUso == null) {
			tipoUso = TipoUsoTextoPadraoPatrimonioEnum.CADASTRO_PATRIMONIO;
		}
		return tipoUso;
	}

	/**
	 * @param tipoUso the tipoUso to set
	 */
	public void setTipoUso(TipoUsoTextoPadraoPatrimonioEnum tipoUso) {
		this.tipoUso = tipoUso;
	}


	/**
	 * @return the textoPadraoImpressao
	 */
	public String getTextoPadraoImpressao() {
		if (textoPadraoImpressao == null) {
			textoPadraoImpressao = "";
		}
		return textoPadraoImpressao;
	}

	/**
	 * @param textoPadraoImpressao the textoPadraoImpressao to set
	 */
	public void setTextoPadraoImpressao(String textoPadraoImpressao) {
		this.textoPadraoImpressao = textoPadraoImpressao;
	}	

	/**
	 * @return the textoTopo
	 */
	public String getTextoTopo() {
		if (textoTopo == null) {
			textoTopo = "";
		}
		return textoTopo;
	}

	/**
	 * @param textoTopo the textoTopo to set
	 */
	public void setTextoTopo(String textoTopo) {
		this.textoTopo = textoTopo;
	}

	/**
	 * @return the textoRodape
	 */
	public String getTextoRodape() {
		if (textoRodape == null) {
			textoRodape = "";
		}
		return textoRodape;
	}

	/**
	 * @param textoRodape the textoRodape to set
	 */
	public void setTextoRodape(String textoRodape) {
		this.textoRodape = textoRodape;
	}

	/**
	 * @return the alturaTopo
	 */
	public Float getAlturaTopo() {
		if (alturaTopo == null) {
			alturaTopo = 4f;
		}
		return alturaTopo;
	}

	/**
	 * @param alturaTopo the alturaTopo to set
	 */
	public void setAlturaTopo(Float alturaTopo) {
		this.alturaTopo = alturaTopo;
	}

	/**
	 * @return the alturaRodape
	 */
	public Float getAlturaRodape() {
		if (alturaRodape == null) {
			alturaRodape = 3f;
		}
		return alturaRodape;
	}

	/**
	 * @param alturaRodape the alturaRodape to set
	 */
	public void setAlturaRodape(Float alturaRodape) {
		this.alturaRodape = alturaRodape;
	}
	
	
	

}
