package iks.market.marketwarehouse.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao

public interface DocGoodsDao {
    @Query("Select * from DocGoods")
    List<DocGoods> getDocumentsGoodList();

    @Insert
    void insertGoods(DocGoods docGoods);
    @Update
    void updateGoods(DocGoods docGoods);
    @Delete
    void deleteGoods(DocGoods docGoods);
}
