package edu.utfpr.guilhermej.sd.stockmarketwebservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * Evento lançado quando ordem de ação é criada, removida, atualizada,
 * quando houve uma transação ou o valor de cotação foi alterado
 */
public class StockEvent implements Serializable{
    @JsonIgnore
    private Object observable;
    private StockEventType eventType;
    private StockOrder newOrder;
    private StockOrder prevOrder;
    private StockOrder buyOrder;
    private StockOrder sellOrder;
    private Stocks tradedStock;

    protected StockEvent(){
        observable = null;
        eventType = null;
        prevOrder = null;
        newOrder = null;
        buyOrder = null;
        sellOrder = null;
        tradedStock = null;
    }

    /**
     * Cria novo evento de ordem de ação criada
     * @param stockOrder ordem criada
     * @param triggerer objeto que lançou o evento
     * @return evento de ordem de ação criada
     */
    public static StockEvent createAddedStockOrderEvent(StockOrder stockOrder, Object triggerer){
        return new StockEvent()
            .setEventType(StockEventType.ADDED)
            .setNewOrder(stockOrder)
            .setObservable(triggerer);
    }

    /**
     * Cria novo evento de ordem de ação removida
     * @param stockOrder ordem removida
     * @param triggerer objeto que lançou o evento
     * @return evento de ordem de ação removida
     */
    public static StockEvent createRemovedStockOrderEvent(StockOrder stockOrder, Object triggerer){
        return new StockEvent()
                .setEventType(StockEventType.REMOVED)
                .setPrevOrder(stockOrder)
                .setObservable(triggerer);
    }

    /**
     * Cria novo evento de ordem de ação atualizada
     * @param previousValue valor antigo da ordem de ação
     * @param newValue novo valor da ordem de ação
     * @param triggerer objeto que lançou o evento
     * @return evento de ordem de ação alterada
     */
    public static StockEvent createUpdatedStockOrderEvent(StockOrder previousValue, StockOrder newValue, Object triggerer){
        return new StockEvent()
                .setEventType(StockEventType.UPDATED)
                .setPrevOrder(previousValue)
                .setNewOrder(newValue)
                .setObservable(triggerer);
    }

    /**
     * Cria novo evento de transação de ações
     * @param buyOrder ordem de compra de ação da transação
     * @param sellOrder ordem de venda de ação da transação
     * @param tradedStock ações transacionadas
     * @param triggerer objeto que lançou o evento
     * @return evento de transação de ações
     */
    public static StockEvent createTradedStockOrderEvent(StockOrder buyOrder, StockOrder sellOrder, Stocks tradedStock, Object triggerer){
        return new StockEvent()
            .setEventType(StockEventType.TRADED)
            .setBuyOrder(buyOrder)
            .setSellOrder(sellOrder)
            .setTradedStock(tradedStock)
            .setObservable(triggerer);
    }

    /**
     * Verifica se determinado acionista tem participou de alguma ordem de ação deste evento.
     * @param holder acionista para verificação
     * @return true se acionista possui participou de alguma ordem de ação deste evento, false caso contrário
     */
    public boolean isParticipant(Stockholder holder){
        if(holder == null)
            return false;
        switch(eventType) {
            case ADDED:
                return getNewOrder() != null &&
                        holder.equals(getNewOrder().getOrderPlacer());
            case REMOVED:
                return getPrevOrder() != null &&
                        holder.equals(getPrevOrder().getOrderPlacer());
            case TRADED:
                return (getBuyOrder() != null &&
                        holder.equals(getBuyOrder().getOrderPlacer())) ||
                        (getSellOrder() != null &&
                        holder.equals(getSellOrder().getOrderPlacer()));
            case UPDATED:
                return getNewOrder() != null &&
                        holder.equals(getNewOrder().getOrderPlacer());
        }
        return false;
    }

    /**
     * Verifica se o evento se relaciona à alguma empresa.
     * A empresa pode se relacionar com o evento se as ações vendidas pelas ordens que
     * causaram este evento são desta empresa, ou se houve uma atualização na cotação das ações desta empresa
     * @param enterprise empresa para verificação
     * @return true se a empresa se relaciona com este evento, false caso contrário
     */
    public boolean isFromEnterprise(String enterprise) {
        if(enterprise == null)
            return false;
        if(enterprise.isEmpty())
            return false;
        switch(eventType) {
            case ADDED:
                return  getNewOrder() != null &&
                            enterprise.trim().equalsIgnoreCase(getNewOrder().getStocks().getEnterprise().trim());
            case REMOVED:
                return  getPrevOrder() != null &&
                            enterprise.trim().equalsIgnoreCase(getPrevOrder().getStocks().getEnterprise().trim());
            case TRADED:
                return  (getBuyOrder() != null &&
                            enterprise.trim().equalsIgnoreCase(getBuyOrder().getStocks().getEnterprise().trim())) ||
                        (getSellOrder() != null &&
                            enterprise.trim().equalsIgnoreCase(getSellOrder().getStocks().getEnterprise().trim()));
            case UPDATED:
                return  getNewOrder() != null &&
                            enterprise.trim().equalsIgnoreCase(getNewOrder().getStocks().getEnterprise().trim());
        }
        return false;
    }

    public Object getObservable() {
        return observable;
    }

    public StockEvent setObservable(Object observable) {
        this.observable = observable;
        return this;
    }

    public StockEventType getEventType() {
        return eventType;
    }

    public StockEvent setEventType(StockEventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public StockOrder getPrevOrder() {
        return prevOrder;
    }

    public StockEvent setPrevOrder(StockOrder prevOrder) {
        this.prevOrder = prevOrder;
        return this;
    }

    public StockOrder getNewOrder() {
        return newOrder;
    }

    public StockEvent setNewOrder(StockOrder newOrder) {
        this.newOrder = newOrder;
        return this;
    }

    public StockOrder getBuyOrder() {
        return buyOrder;
    }

    public StockEvent setBuyOrder(StockOrder buyOrder) {
        this.buyOrder = buyOrder;
        return this;
    }

    public StockOrder getSellOrder() {
        return sellOrder;
    }

    public StockEvent setSellOrder(StockOrder sellOrder) {
        this.sellOrder = sellOrder;
        return this;
    }

    public Stocks getTradedStock() {
        return tradedStock;
    }

    public StockEvent setTradedStock(Stocks tradedStock) {
        this.tradedStock = tradedStock;
        return this;
    }

    /**
     * Enumeração descreve possível tipos de eventos
     */
    public enum StockEventType {
        ADDED, REMOVED, UPDATED, TRADED
    }
}
