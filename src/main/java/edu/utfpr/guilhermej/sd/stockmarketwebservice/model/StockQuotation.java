package edu.utfpr.guilhermej.sd.stockmarketwebservice.model;

import java.io.Serializable;

/**
 * Representação de uma cotação de ação, contém o valor da cotação e a empresa cotada
 */
public class StockQuotation implements Serializable{
    private String enterprise;
    private Double price;

    public String getEnterprise(){
        return enterprise;
    }

    public StockQuotation setEnterprise(String enterprise) {
        this.enterprise = enterprise;
        return this;
    }

    public Double getPrice() {
        return price;
    }

    public StockQuotation setPrice(Double price) {
        this.price = price;
        return this;
    }
}
