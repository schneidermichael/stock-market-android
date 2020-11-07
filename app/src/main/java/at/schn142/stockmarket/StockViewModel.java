package at.schn142.stockmarket;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import at.schn142.stockmarket.model.Stock;

public class StockViewModel extends AndroidViewModel {

    public static final String TAG = "StockViewModel";

    private StockRepository mRepository;

    private LiveData<List<Stock>> mSearchStocks;

    private LiveData<List<Stock>> mAllStocks;

    public StockViewModel(Application application) {
        super(application);
        mRepository = new StockRepository(application);
        mAllStocks = mRepository.getAllStocks();
        mSearchStocks = mRepository.getSearchStocks();
    }

    public LiveData<List<Stock>> getSearchStock() {
        return mSearchStocks;
    }

    public LiveData<List<Stock>> getAllStocks() { return mAllStocks;}

    public void insert(Stock stock) {
        mRepository.insert(stock);
    }

    public void deleteStock(Stock stock) {mRepository.deleteStock(stock);}

    public void deleteAll() {mRepository.deleteAll();}

    public void searchIexCloud(String searchQuery) {
        mRepository.searchIexCloud(searchQuery);
    }

}
