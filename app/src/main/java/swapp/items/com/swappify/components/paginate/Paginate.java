package swapp.items.com.swappify.components.paginate;

import android.support.v7.widget.RecyclerView;

import swapp.items.com.swappify.components.paginate.recycler.RecyclerPaginate;


public abstract class Paginate {

    public static final int SCROLL_UP = 1;
    public static final int SCROLL_DOWN = 2;

    public interface Callbacks {
        void onLoadMore(int direction);

        boolean isLoading();

        boolean hasLoadedAllItems();
    }

    abstract public void setHasMoreDataToLoad(boolean hasMoreDataToLoad);

    abstract public void unbind();

    public static RecyclerPaginate.Builder with(RecyclerView recyclerView, Callbacks callback) {
        return new RecyclerPaginate.Builder(recyclerView, callback);
    }
}