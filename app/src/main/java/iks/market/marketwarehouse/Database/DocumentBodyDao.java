package iks.market.marketwarehouse.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DocumentBodyDao {
    @Query("Select * from DocBody")
    List<DocBody> getDocBodyList();
    @Query("Select * from DocBody WHERE qty != qtypredict")
    List<DocBody> getDocBodyListDifference();
    @Query("SELECT * FROM DocBody WHERE docnumber LIKE :id")
    List<DocBody> getBodyByNumber(String id);
    @Query("SELECT * FROM DocBody WHERE barcode LIKE :barcode AND docnumber LIKE :number")
    List<DocBody> getGoodsByBarcode(String barcode, String number);
    @Query("SELECT * FROM DocBody WHERE code LIKE :article")
    List<DocBody> getGoodsByArticle(String article);
    @Insert
    void insertDocuments(DocBody docBody);
    @Update
    void updateDocuments(DocBody docBody);
    @Delete
    void deleteDocuments(DocBody docBody);
    @Query("UPDATE DocBody SET qty=qty+ :number WHERE barcode LIKE :barcode")
    void updateQuantity(String barcode, int number);


}
