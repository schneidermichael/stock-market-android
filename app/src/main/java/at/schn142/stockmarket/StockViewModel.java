package at.schn142.stockmarket;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.anychart.chart.common.dataentry.DataEntry;

import java.util.List;

import at.schn142.stockmarket.model.Stock;
import at.schn142.stockmarket.model.StockRange;

public class StockViewModel extends AndroidViewModel {

    public static final String TAG = "StockViewModel";

    private StockRepository mRepository;

    private LiveData<List<Stock>> mSearchStocks;

    private LiveData<List<Stock>> mAllStocks;

    private LiveData<List<DataEntry>> mData;

    public StockViewModel(Application application) {
        super(application);
        mRepository = new StockRepository(application);
        mAllStocks = mRepository.getAllStocks();
        mSearchStocks = mRepository.getSearchStocks();
        mData = mRepository.getData();
    }

    public LiveData<List<Stock>> getSearchStock() {
        return mSearchStocks;
    }

    public LiveData<List<Stock>> getAllStocks() { return mAllStocks;}

    public void insert(Stock stock) {
        mRepository.insert(stock);
    }

    public void update(Stock stock) {mRepository.update(stock); }

    public void delete(Stock stock) {mRepository.delete(stock);}

    public void deleteAll() {mRepository.deleteAll();}

    public void search(String searchQuery) {
        mRepository.searchIexCloud(searchQuery);
    }

    public Stock searchStock(String symbol) throws InterruptedException { return mRepository.searchStock(symbol);}

    public void getDataEntryForOHLC(String symbol, StockRange range) { mRepository.getDataEntryForOHLC(symbol,range);}

    public LiveData<List<DataEntry>> getData() {return mData;}

}
