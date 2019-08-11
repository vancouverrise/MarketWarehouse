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
    @Query("SELECT * FROM DocBody WHERE docnumber LIKE :id")
    List<DocBody> getBodyByNumber(String id);
    @Insert
    void insertDocuments(DocBody docBody);
    @Update
    void updateDocuments(DocBody docBody);
    @Delete
    void deleteDocuments(DocBody docBody);

}
