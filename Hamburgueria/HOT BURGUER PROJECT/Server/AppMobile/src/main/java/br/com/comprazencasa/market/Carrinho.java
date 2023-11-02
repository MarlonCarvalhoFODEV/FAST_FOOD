package br.com.comprazencasa.market;

import br.com.comprazencasa.itens.ITProduct;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.HashMap;

public class Carrinho extends HashMap<Integer, ITProduct> implements Serializable {
    double valorCompraPreDefault ;
    private double valorCompra;

    public double adicionarAoCarrinho(ITProduct item) {
        put(item.getId(), item);
        DecimalFormat df = new DecimalFormat("0.##");

        String price = df.format(valorCompra += Double.valueOf(item.getPerPriceMultiply()));

        return valorCompra = Double.valueOf(price.replace(",", "."));
    }

    public void deletarDoCarrinho(ITProduct item, boolean directClick) {
        DecimalFormat df = new DecimalFormat("0.##");

        valorCompra -= item.getPerPriceMultiply();
        valorCompra = Double.valueOf(df.format(valorCompra).replace(",", "."));

        if (item.getCount() > 1 || item.getCount() > 50) adicionarAoCarrinho(item);

        if (containsKey(item.getId()) && (item.getCount() == 1 || item.getCount() == 50)
                || directClick == true) remove(item.getId());
    }

    public int getTamanhoDoCarrinho() {
        int size = 0;
        for (ITProduct p : values()) {
            size += p.isWeight() ? 1 : p.getCount();
        }
        return size;
    }

    public void esvaziarCarrinho() {
        clear();
        valorCompra = 0.00;
    }

    public double getValorCompra() {
        if (getTamanhoDoCarrinho() >= 1) {
            valorCompra = 0;
            for (ITProduct p : values()) {
                valorCompra += p.getPerPriceMultiply();
            }

            DecimalFormat df = new DecimalFormat("0.##");
            return (valorCompra = Double.valueOf(df.format(valorCompra).replace(",", ".")));
        } else{
            valorCompra = 0;
            return valorCompraPreDefault>0?valorCompraPreDefault:valorCompra;
        }
    }

    public double getValorCompraPreDefault() {
        return valorCompraPreDefault;
    }

    public void setValorCompraPreDefault(double valorCompraPreDefault) {
        this.valorCompraPreDefault = valorCompraPreDefault;
    }

    public void setValorCompra(double valorCompra) {
        this.valorCompra = valorCompra;
    }

    public double addValorCompra(double valor) {
        valorCompra = 0;
        for (ITProduct p : values()) {
            valorCompra += p.getPerPriceMultiply();
        }
        valorCompra += valor;
        DecimalFormat df = new DecimalFormat("0.##");

        return (valorCompra = Double.valueOf(df.format(valorCompra).replace(",", ".")));
    }
}