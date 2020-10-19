package at.schn142.stockmarket;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.List;

public class StockViewModel extends AndroidViewModel {

    private StockRepository mRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private List<Stock> mAllStocks;

    public StockViewModel(Application application) {
        super(application);
        mRepository = new StockRepository(application);
        mAllStocks = mRepository.getAllStocks();
    }

    List<Stock> getAllStock() {
        return mAllStocks;
    }

    void insert(Stock stock) {
        mRepository.insert(stock);
    }
}
