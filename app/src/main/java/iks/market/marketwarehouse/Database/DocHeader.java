package iks.market.marketwarehouse.Database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "DocHeader")
public class DocHeader {
    @NonNull
    @PrimaryKey
    public String documentNumber;

    @ColumnInfo(name = "CODE")
    public String code;
    @ColumnInfo(name = "NAME")
    public String name;
    @ColumnInfo(name = "DESCRIPTION")
    public String description;

    public DocHeader(String documentNumber, String code, String name, String description){
        this.documentNumber = documentNumber;
        this.code = code;
        this.name = name;
        this.description = description;
    }

    @Ignore
    public DocHeader(String code, String name, String description){
        this.code = code;
        this.name = name;
        this.description = description;
    }

}
