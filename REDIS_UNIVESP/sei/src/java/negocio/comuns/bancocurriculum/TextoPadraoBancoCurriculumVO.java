package negocio.comuns.bancocurriculum;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;

public class TextoPadraoBancoCurriculumVO extends SuperVO {

    private Integer codigo;
    private String nome;
    private String tipo;
    private String situacao;
    private String texto;
    private String imgCima;
    private String imgBaixo;
    private List listaTagUtilizado;
    private String caminhoImagemPadraoCima;
    private String caminhoImagemPadraoBaixo;

    public void substituirTagsTextoPadrao(TextoPadraoBancoCurriculumVO textoPadrao, VagasVO vaga) {
        if (textoPadrao.getTexto().contains("#Nome_Empresa")) {
            textoPadrao.setTexto(Uteis.trocarHashTag("#Nome_Empresa", vaga.getParceiro().getNome(), textoPadrao.getTexto()));
        }
        if (textoPadrao.getTexto().contains("#Cargo_Vaga")) {
            textoPadrao.setTexto(Uteis.trocarHashTag("#Cargo_Vaga", vaga.getCargo(), textoPadrao.getTexto()));
        }
        if (textoPadrao.getTexto().contains("#Local_Vaga")) {
//            textoPadrao.setTexto(Uteis.trocarHashTag("#Local_Vaga", vaga.getCidade().getNome() + "/" + vaga.getCidade().getEstado().getSigla(), textoPadrao.getTexto()));
        }
        if (textoPadrao.getTexto().contains("#AreaProfissional_Vaga")) {
            textoPadrao.setTexto(Uteis.trocarHashTag("#AreaProfissional_Vaga", vaga.getAreaProfissional().getDescricaoAreaProfissional(), textoPadrao.getTexto()));
        }
        if (textoPadrao.getTexto().contains("#Codigo_Vaga")) {
            textoPadrao.setTexto(Uteis.trocarHashTag("#Codigo_Vaga", vaga.getCodigo().toString(), textoPadrao.getTexto()));
        }
        if (textoPadrao.getTexto().contains("#Horario_Vaga")) {
            textoPadrao.setTexto(Uteis.trocarHashTag("#Horario_Vaga", vaga.getHorarioTrabalho(), textoPadrao.getTexto()));
        }
    }

    public String getNome() {
        if (nome == null) {
            nome = "";
        }
        return nome;
    }

    public String getMensagemComLayoutTextoPadrao() {
        return getMensagemComLayoutTextoPadrao("");
    }

    public String getMensagemComLayoutTextoPadrao(String mensagem) {

        // obeter o ip ou dns;
//        String dominio = UteisEmail.getURLAplicacao("/imagens/email/");
        String dominio = null;
        try {
            dominio = "./resources/imagens/email/";
        } catch (Exception e) {
          //  //System.out.println("Comunicacao Erro:" + e.getMessage());
        }
//        String img_cima = dominio + "cima_sei.jpg";
//        String img_baixo = dominio + "baixo_sei.jpg";
//        String img_meio_cima = dominio + "meio_cima_default1.png";
//        String img_meio_baixo = dominio + "meio_baixo_default1.png";
        setCaminhoImagemPadraoCima(dominio + "cima_sei.jpg");
        setCaminhoImagemPadraoBaixo(dominio + "baixo_sei.jpg");

        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/xhtml; charset=UTF-8\" /></head><body>");
        sb.append("<div class=\"Section1\">");
        sb.append("<div>");
        sb.append("<table class=\"MsoNormalTable\" style=\"width: 275px; mso-cellspacing: 0cm; mso-yfti-tbllook: 1184; mso-padding-alt: 0cm 0cm 0cm 0cm;\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"300\">");
        sb.append("<tbody>");
        sb.append("<tr style=\"height: 59.25pt; mso-yfti-irow: 0; mso-yfti-firstrow: yes;\">");
        sb.append("<td style=\"height: 59.25pt; padding: 0cm;\" colspan=\"3\">");
        sb.append("<p class=\"MsoNormal\" style=\"margin:0cm;margin-bottom:.0001pt\"><span style=\"font-family: Arial; color: black; font-size: 10pt; mso-no-proof: yes;\"><img id=\"_x0000_i1025\" src=\"" + getCaminhoImagemPadraoCima() + "\" alt=\"" + getImgCima() + "\" width=\"626\" height=\"77\" border=\"0\" /></span></p>");
        sb.append("</td>");
        sb.append("</tr>");
        //sb.append("<tr style=\"mso-yfti-irow:1\">");
        //sb.append("<td style=\"padding:0cm 0cm 0cm 0cm\" colspan=\"3\">");
        //sb.append("<p class=\"MsoNormal\" style=\"margin:0cm;margin-bottom:.0001pt\"><span style=\"font-family: Arial; color: black; font-size: 10pt; mso-no-proof: yes;\"><img id=\"_x0000_i1027\" src=\"" + img_meio_cima + "\" border=\"0\" alt=\"" + img_meio_cima + "\" width=\"626\" height=\"20\" /></span></p>");
        //sb.append("</td>");
        //sb.append("</tr>");
        sb.append("<tr style=\"mso-yfti-irow:2\">");
        //sb.append("<td style=\"width: 9pt; background: #DCD5FE; padding: 0cm;\" width=\"12\">");
        //sb.append("<p><span style=\"font-size:10.0pt;font-family:Arial;color:black\">&nbsp;</span></p>");
        //sb.append("</td>");
        sb.append("<td style=\"width: 469.5pt; background: #FFFFFF; padding: 0cm;\" width=\"626\">");
        sb.append("<p class=\"MsoNormal\" style=\"margin:0cm;margin-bottom:.0001pt\"><span style=\"font-family: Arial; color: black; font-size: 10pt;\">&nbsp;</span></p>");
        sb.append("<p class=\"MsoNormal\" style=\"margin:0cm;margin-bottom:.0001pt\">" + mensagem + "<span style=\"font-family: Arial; color: black; font-size: 10pt;\">&nbsp;</span></p>");
        sb.append("</span></td>");
        //sb.append("<td style=\"width: 9pt; background: #DCD5FE; padding: 0cm;\" width=\"12\">");
        //sb.append("<p class=\"MsoNormal\" style=\"margin:0cm;margin-bottom:.0001pt\"><span style=\"font-family: Arial; color: black; font-size: 10pt;\">&nbsp;</span></p>");
        //sb.append("</td>");
        sb.append("</tr>");
        //sb.append("<tr style=\"mso-yfti-irow:3\">");
        //sb.append("<td style=\"padding:0cm 0cm 0cm 0cm\" colspan=\"3\">");
        //sb.append("<p class=\"MsoNormal\" style=\"margin:0cm;margin-bottom:.0001pt\"><span style=\"font-family: Arial; color: black; font-size: 10pt; mso-no-proof: yes;\"><img id=\"_x0000_i1027\" src=\"" + img_meio_baixo + "\" border=\"0\" alt=\"" + img_meio_baixo + "\" width=\"626\" height=\"20\" /></span></p>");
        //sb.append("</td>");
        //sb.append("</tr>");
        sb.append("<tr style=\"height: 43.5pt; mso-yfti-irow: 4; mso-yfti-lastrow: yes;\">");
        sb.append("<td style=\"height: 43.5pt; padding: 0cm;\" colspan=\"3\">");
        sb.append("<p class=\"MsoNormal\" style=\"margin:0cm;margin-bottom:.0001pt\"><span style=\"font-family: Arial; color: black; font-size: 10pt; mso-no-proof: yes;\"><img id=\"_x0000_i1028\" src=\"" + getCaminhoImagemPadraoBaixo() + "\" border=\"0\" alt=\"" + getImgBaixo() + "\" width=\"626\" height=\"59\" /></span></p>");
        sb.append("</td>");
        sb.append("</tr>");
        sb.append("</tbody>");
        sb.append("</table>");
        sb.append("</div>");
        sb.append("<p class=\"MsoNormal\" style=\"margin:0cm;margin-bottom:.0001pt\"><span style=\"color: black;\">&nbsp;</span></p>");
        sb.append("</div>");
        sb.append("</body></html>");
        return sb.toString();
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo_Apresentar() {
        if (getTipo().equals("boasVindasAluno")) {
            return "Boas Vindas Aluno";
        } else if (getTipo().equals("boasVindasParceiro")) {
            return "Boas Vindas Parceiro";
        } else if (getTipo().equals("notaAbaixoMedia")) {
            return "Nota Abaixo da Média";
        } else if (getTipo().equals("candidatoSelecionado")) {
            return "Candidato Selecionado";
        } else if (getTipo().equals("candidatoDesclassificado")) {
            return "Candidato Desclassificado";
        } else if (getTipo().equals("vagaEncerradaCandidato")) {
            return "Vaga Encerrada para o Candidato";
        } else if (getTipo().equals("vagaEncerradaParceiro")) {
            return "Vaga Encerrada para o Parcerio";
        } else if (getTipo().equals("iminenciaExpiracaoVaga")) {
            return "Iminência de Expiração de Vaga";
        } else if (getTipo().equals("iminenciaExpiracaoCurriculum")) {
            return "Iminência de Expiração de Currículum";
        } else {
            return "Notificar Aluno sobre Sugestão de Vaga";
        }
    }

    public String getTipo() {
        if (tipo == null) {
            tipo = "";
        }
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getSituacao() {
        if (situacao == null) {
            situacao = "EC";
        }
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getTexto() {
        if (texto == null) {
            texto = "";
        }
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getSituacao_Apresentar() {
        if (this.getSituacao().equals("CA")) {
            return "Cancelado";
        }
        if (this.getSituacao().equals("AT")) {
            return "Ativado";
        }
        return "Em Construção";
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public List getListaTagUtilizado() {
        if (listaTagUtilizado == null) {
            listaTagUtilizado = new ArrayList(0);
        }
        return listaTagUtilizado;
    }

    public void setListaTagUtilizado(List listaTagUtilizado) {
        this.listaTagUtilizado = listaTagUtilizado;
    }

    public String getCaminhoImagemPadraoCima() {
        if (caminhoImagemPadraoCima == null) {
            caminhoImagemPadraoCima = "";
        }
        return caminhoImagemPadraoCima;
    }

    public void setCaminhoImagemPadraoCima(String caminhoImagemPadraoCima) {
        this.caminhoImagemPadraoCima = caminhoImagemPadraoCima;
    }

    public String getCaminhoImagemPadraoBaixo() {
        if (caminhoImagemPadraoBaixo == null) {
            caminhoImagemPadraoBaixo = "";
        }
        return caminhoImagemPadraoBaixo;
    }

    public void setCaminhoImagemPadraoBaixo(String caminhoImagemPadraoBaixo) {
        this.caminhoImagemPadraoBaixo = caminhoImagemPadraoBaixo;
    }

    public String getImgBaixo() {
        if (imgBaixo == null) {
            imgBaixo = "";
        }
        return imgBaixo;
    }

    public void setImgBaixo(String imgBaixo) {
        this.imgBaixo = imgBaixo;
    }

    public String getImgCima() {
        if (imgCima == null) {
            imgCima = "";
        }
        return imgCima;
    }

    public void setImgCima(String imgCima) {
        this.imgCima = imgCima;
    }
}
