package at.schn142.stockmarket;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.anychart.chart.common.dataentry.DataEntry;

import java.util.List;

import at.schn142.stockmarket.model.Stock;
import at.schn142.stockmarket.model.StockRange;

/**
 * This class represents ViewModel
 *
 * @author michaelschneider
 * @version 1.0
 */
public class ViewModel extends AndroidViewModel {

    private Repository mRepository;

    private LiveData<List<Stock>> mSearchStocks;

    private LiveData<List<Stock>> mAllStocks;

    private LiveData<List<DataEntry>> mData;

    public ViewModel(Application application) {
        super(application);
        mRepository = new Repository(application);
        mAllStocks = mRepository.getAllStocks();
        mSearchStocks = mRepository.getSearchStocks();
        mData = mRepository.getData();
    }

    public void insert(Stock stock) {
        mRepository.insert(stock);
    }

    public void delete(Stock stock) {
        mRepository.delete(stock);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }

    public void updateAll() {
        mRepository.updateAll();
    }

    public void search(String searchQuery) {
        mRepository.search(searchQuery);
    }


    public LiveData<List<Stock>> getSearchStock() {
        return mSearchStocks;
    }

    public LiveData<List<Stock>> getAllStocks() {
        return mAllStocks;
    }

    public Stock searchStock(String symbol) {
        return mRepository.searchStock(symbol);
    }

    public void getOLHCDataEntry(String symbol, StockRange range) {
        mRepository.getOLHCDataEntry(symbol, range);
    }

    public LiveData<List<DataEntry>> getData() {
        return mData;
    }

}
