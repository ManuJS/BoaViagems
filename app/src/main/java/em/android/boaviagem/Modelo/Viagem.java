package em.android.boaviagem.Modelo;

import java.util.Date;

/**
 * Created by Emanuelle Menali on 07/07/2016.
 */
public class Viagem {
    private Long id;
    private String destino;
    private Integer tipoViagem;
    private Date dataChegada;
    private Date dataSaida;
    private double orcamento;
    private Integer quantidadeDePessoas;

    public Viagem(){}

    public Viagem(Long id, String destino, Integer tipoViagem, Date dataChegada, Date dataSaida, double orcamento, Integer quantidadeDePessoas){
        this.id = id;
        this.destino = destino;
        this.tipoViagem = tipoViagem;
        this.dataChegada = dataChegada;
        this.dataSaida = dataSaida;
        this.orcamento = orcamento;
        this.quantidadeDePessoas = quantidadeDePessoas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Integer getTipoViagem() {
        return tipoViagem;
    }

    public void setTipoViagem(Integer tipoViagem) {
        this.tipoViagem = tipoViagem;
    }

    public Date getDataChegada() {
        return dataChegada;
    }

    public void setDataChegada(Date dataChegada) {
        this.dataChegada = dataChegada;
    }

    public Date getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(Date dataSaida) {
        this.dataSaida = dataSaida;
    }

    public double getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(double orcamento) {
        this.orcamento = orcamento;
    }

    public Integer getQuantidadeDePessoas() {
        return quantidadeDePessoas;
    }

    public void setQuantidadeDePessoas(Integer quantidadeDePessoas) {
        this.quantidadeDePessoas = quantidadeDePessoas;
    }
}
