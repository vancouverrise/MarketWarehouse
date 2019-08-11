package iks.market.marketwarehouse.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DocHeaderDao {
    @Query("Select * from DocHeader")
    List<DocHeader> getDocumentsHeaderList();
    @Insert
    void insertDocuments(DocHeader docHeader);
    @Update
    void updateDocuments(DocHeader docHeader);
    @Delete
    void deleteDocuments(DocHeader docHeader);

}
