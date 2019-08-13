package iks.market.marketwarehouse.GoodsInDocument;

public class GoodsInsideModel {
    private String name, barcode, article, inpack, qty, qtyPredict;

    public String getInpack() {
        return inpack;
    }

    public void setInpack(String inpack) {
        this.inpack = inpack;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQtyPredict() {
        return qtyPredict;
    }

    public void setQtyPredict(String qtyPredict) {
        this.qtyPredict = qtyPredict;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}
