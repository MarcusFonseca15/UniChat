package visual;

public class Mensagem {
    private String texto;
    private boolean enviada; // true se foi enviada por mim, false se recebida

    public Mensagem(String texto, boolean enviada) {
        this.texto = texto;
        this.enviada = enviada;
    }

    public String getTexto() {
        return texto;
    }

    public boolean isEnviada() {
        return enviada;
    }
}
