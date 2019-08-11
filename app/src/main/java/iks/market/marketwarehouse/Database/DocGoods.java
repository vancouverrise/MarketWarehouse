package iks.market.marketwarehouse.Database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity (tableName = "DocGoods", primaryKeys = {"code", "barcode", "inpack"})
public class DocGoods {
@NonNull
    public String code;
@NonNull
    public String barcode;
@NonNull
    public String inpack;
    @ColumnInfo
    public String name;


    public DocGoods (String code, String barcode, String inpack, String name){
        this.code = code;
        this.barcode = barcode;
        this.inpack = inpack;
        this.name = name;

    }
}
