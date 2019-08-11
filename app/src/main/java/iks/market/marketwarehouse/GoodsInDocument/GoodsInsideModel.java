package iks.market.marketwarehouse.GoodsInDocument;

public class GoodsInsideModel {
    private String name, barcode, article, inpack;

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
}
