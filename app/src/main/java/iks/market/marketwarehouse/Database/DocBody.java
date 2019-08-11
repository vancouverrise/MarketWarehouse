package iks.market.marketwarehouse.Database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity (tableName = "DocBody", primaryKeys = {"docnumber", "code", "barcode", "inpack"})
public class DocBody {

    @NonNull
    public String docnumber;
    @NonNull
    public String code;
    @NonNull
    public String barcode;
    @NonNull
    public String inpack;
    @ColumnInfo(name = "NAME")
    public String name;
    @ColumnInfo(name = "QTY")
    public String qty;
    @ColumnInfo(name = "QTYPREDICT")
    public String qtypredict;

    public DocBody(String docnumber, String code, String barcode, String inpack, String name, String qty, String qtypredict){

        this.docnumber = docnumber;
        this.code = code;
        this.barcode = barcode;
        this.inpack = inpack;
        this.name = name;
        this.qty = qty;
        this.qtypredict = qtypredict;
    }

}
