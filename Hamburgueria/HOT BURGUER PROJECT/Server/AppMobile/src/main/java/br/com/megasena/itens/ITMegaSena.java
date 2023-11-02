package br.com.megasena.itens;

import java.io.Serializable;

public class ITMegaSena implements Serializable {

    private int idDB ;
    private int concurso ;
    private String timeDiff ;
    private String local ;
    private String data ;
    private int coluna1 ;
    private int coluna2 ;
    private int coluna3 ;
    private int coluna4 ;
    private int coluna5 ;
    private int coluna6 ;
    private int ganhadoreFx1 ;
    private int ganhadoreFx2 ;
    private int ganhadoreFx3 ;
    private String rateioFx1 ;
    private String rateioFx2 ;
    private String rateioFx3 ;
    private String cidade ;
    private String valorArrecadado ;
    private String valorEstimadoProximoConcurso ;
    private String valorAcumulado ;
    private String valorAcumuladoProximoConcursoFinalCinc ;
    private String valorAcumuladoMegaVirada ;
    private boolean acumulado ;
    private boolean sorteioEspecial ;
    private String observacao ;
    private boolean haveMore ;

    public int getIdDB() {
        return idDB;
    }

    public void setIdDB(int idDB) {
        this.idDB = idDB;
    }

    public String getTimeDiff() {
        return timeDiff;
    }

    public void setTimeDiff(String timeDiff) {
        this.timeDiff = timeDiff;
    }

    public int getConcurso() {
        return concurso;
    }

    public void setConcurso(int concurso) {
        this.concurso = concurso;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getColuna1() {
        return coluna1;
    }

    public void setColuna1(int coluna1) {
        this.coluna1 = coluna1;
    }

    public int getColuna2() {
        return coluna2;
    }

    public void setColuna2(int coluna2) {
        this.coluna2 = coluna2;
    }

    public int getColuna3() {
        return coluna3;
    }

    public void setColuna3(int coluna3) {
        this.coluna3 = coluna3;
    }

    public int getColuna4() {
        return coluna4;
    }

    public void setColuna4(int coluna4) {
        this.coluna4 = coluna4;
    }

    public int getColuna5() {
        return coluna5;
    }

    public void setColuna5(int coluna5) {
        this.coluna5 = coluna5;
    }

    public int getColuna6() {
        return coluna6;
    }

    public void setColuna6(int coluna6) {
        this.coluna6 = coluna6;
    }

    public int getGanhadoreFx1() {
        return ganhadoreFx1;
    }

    public void setGanhadoreFx1(int ganhadoreFx1) {
        this.ganhadoreFx1 = ganhadoreFx1;
    }

    public int getGanhadoreFx2() {
        return ganhadoreFx2;
    }

    public void setGanhadoreFx2(int ganhadoreFx2) {
        this.ganhadoreFx2 = ganhadoreFx2;
    }

    public int getGanhadoreFx3() {
        return ganhadoreFx3;
    }

    public void setGanhadoreFx3(int ganhadoreFx3) {
        this.ganhadoreFx3 = ganhadoreFx3;
    }

    public String getRateioFx1() {
        return rateioFx1;
    }

    public void setRateioFx1(String rateioFx1) {
        this.rateioFx1 = rateioFx1;
    }

    public String getRateioFx2() {
        return rateioFx2;
    }

    public void setRateioFx2(String rateioFx2) {
        this.rateioFx2 = rateioFx2;
    }

    public String getRateioFx3() {
        return rateioFx3;
    }

    public void setRateioFx3(String rateioFx3) {
        this.rateioFx3 = rateioFx3;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getValorArrecadado() {
        return valorArrecadado;
    }

    public void setValorArrecadado(String valorArrecadado) {
        this.valorArrecadado = valorArrecadado;
    }

    public String getValorEstimadoProximoConcurso() {
        return valorEstimadoProximoConcurso;
    }

    public void setValorEstimadoProximoConcurso(String valorEstimadoProximoConcurso) {
        this.valorEstimadoProximoConcurso = valorEstimadoProximoConcurso;
    }

    public String getValorAcumulado() {
        return valorAcumulado;
    }

    public void setValorAcumulado(String valorAcumulado) {
        this.valorAcumulado = valorAcumulado;
    }

    public String getValorAcumuladoProximoConcursoFinalCinc() {
        return valorAcumuladoProximoConcursoFinalCinc;
    }

    public void setValorAcumuladoProximoConcursoFinalCinc(String valorAcumuladoProximoConcursoFinalCinc) {
        this.valorAcumuladoProximoConcursoFinalCinc = valorAcumuladoProximoConcursoFinalCinc;
    }

    public String getValorAcumuladoMegaVirada() {
        return valorAcumuladoMegaVirada;
    }

    public void setValorAcumuladoMegaVirada(String valorAcumuladoMegaVirada) {
        this.valorAcumuladoMegaVirada = valorAcumuladoMegaVirada;
    }

    public boolean isAcumulado() {
        return acumulado;
    }

    public void setAcumulado(boolean acumulado) {
        this.acumulado = acumulado;
    }

    public boolean isSorteioEspecial() {
        return sorteioEspecial;
    }

    public void setSorteioEspecial(boolean sorteioEspecial) {
        this.sorteioEspecial = sorteioEspecial;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public boolean isHaveMore() {
        return haveMore;
    }

    public void setHaveMore(boolean haveMore) {
        this.haveMore = haveMore;
    }

}
