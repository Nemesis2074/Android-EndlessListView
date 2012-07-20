package net.nemesis2074.endless;

import java.util.ArrayList;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @author Adair Castillo (Nemesis2074)
 */
public abstract class EndlessListFragment<T> extends ListFragment implements OnScrollListener {
	
	/*++++++++++++++++++++ VARIABLES Y CONSTANTES ++++++++++++++++++++*/
	
	private ArrayList<T> items = new ArrayList<T>();
	private ArrayAdapter<T> adapter;
	
	private View footer;
	private ProgressBar progress;
	private TextView text;

	private int visibleThreshold = 2;
    private int previousTotalItems;
    private boolean isLoading = true;
    private boolean thereIsMoreItems = true;
    
    /*++++++++++++++++++++ INIT ++++++++++++++++++++*/
    
    /**
     * Inicializa el ListView con el Adapter por default.
     */
    public void initListView(){
    	getListView().addFooterView(footer, null, false);
    	setAdapter(adapter);
    	getListView().setOnScrollListener(this);
    	if(items.size() == 0){
    		refreshItems();
    	}
    }
    
    /**
     * Inicializa el ListView con un Adapter.
     */
    public void initListView(ArrayAdapter<T> adapter){
    	getListView().addFooterView(footer, null, false);
    	setAdapter(adapter);
    	getListView().setOnScrollListener(this);
    	if(items.size() == 0){
    		refreshItems();
    	}
    }
    
    /**
     * Inicializa el ListView con un View como cabecera de la lista.
     */
    public void initListView(View header){
    	setListAdapter(null);
    	getListView().addHeaderView(header, null, false);
    	initListView(adapter);
    }
    
    /**
     * Inicializa el ListView con un Adapter y un View de cabecera.
     */
    public void initListView(ArrayAdapter<T> adapter, View header){
    	setListAdapter(null);
    	getListView().addHeaderView(header, null, false);
    	initListView(adapter);
    }
    
    /*++++++++++++++++++++ METODOS ++++++++++++++++++++*/
    
    /**
     * Retorna los elementos mostrados en la lista.
     */
    protected ArrayList<T> getListItems(){
    	return items;
    }
    
    /**
     * Asigna la lista de elementos a mostrar.
     */
    protected void setListItems(ArrayList<T> items){
    	this.items = items;
    }
    
    /**
     * Retorna el adapter del ListView
     */
    protected ArrayAdapter<T> getAdapter(){
    	return this.adapter;
    }
    
    /**
     * Asigna el adaptar a utilizar en el ListView
     */
    protected void setAdapter(ArrayAdapter<T> adapter){
    	if(getListAdapter() == null){
    		this.adapter = adapter;
    		setListAdapter(this.adapter);
    	}
    }
    
    /**
     * Asigna el texto a utilizar como indicador de "Cargando..."
     */
    public void setIndicatorText(CharSequence text){
    	if(this.text != null){
    		this.text.setText(text);
    	}
    }
    
    /**
     * Asigna el texto a utilizar como indicador de "Cargando..."
     */
    public void setIndicatorText(int resid){
    	if(this.text != null){
    		this.text.setText(resid);
    	}
    }
    
    /*++++++++++++++++++++ MANIPULACION DE ITEMS ++++++++++++++++++++*/
    
    /**
     * Agrega una lista de elementos al final de la lista. 
     */
    public void addItems(ArrayList<T> items){
    	progress.setVisibility(View.GONE);
    	if(items.size()>0){
    		this.items.addAll(items);
    		adapter.notifyDataSetChanged();
    	}else{
    		thereIsMoreItems = false;
    	}
    }
    
    /**
     * Agrega un elementos al principio de la lista.
     */
    public void addItem(T item){
    	progress.setVisibility(View.GONE);
    	items.add(0, item);
    	adapter.notifyDataSetChanged();
    }
    
    /**
     * Agrega un elemento al final de la lista. 
     */
    public void addItemAtLast(T item){
    	progress.setVisibility(View.GONE);
    	items.add(item);
    	adapter.notifyDataSetChanged();
    }
    
    /**
     * Elimina un elemento de la lista.
     */
    public void deleteItem(T item){
    	items.remove(item);
    	adapter.notifyDataSetChanged();
    }
    
    /**
     * Elimina un elemento de la lista.
     */
    public void deleteItem(int index){
    	items.remove(index);
    	adapter.notifyDataSetChanged();
    }
    
    /**
     * Elimina todos los elementos de la lista y solicita que se carguen nuevamente llamando al metodo refreshItems()
     */
    public void refresh(){
    	this.items.clear();
    	refreshItems();
    }
    
    /**
     * Se ejecuta para cargar los primeros elementos de la lista y cuando se llama al metodo refresh()  
     */
    public abstract void refreshItems();
    
    /**
     * Se ejecuta cuando el usuario ha llegado al final de la lista y es necesario cargar mas elementos.
     */
    public abstract void loadMoreItems(int totalItems);
    
    /*++++++++++++++++++++ EVENTOS ++++++++++++++++++++*/
    
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	if(footer == null){
    		footer = inflater.inflate(R.layout.loading_indicator, null, false);
    		progress = (ProgressBar)footer.findViewById(R.id.loading_indicator_progressbar);
    		text = (TextView)footer.findViewById(R.id.loading_indicator_text);
    	}
    	
    	return super.onCreateView(inflater, container, savedInstanceState);
    }
    
    @Override
    public final void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    	totalItemCount -= getListView().getHeaderViewsCount();
    	totalItemCount -= getListView().getFooterViewsCount();
    	
        if (isLoading) {
            if (totalItemCount > previousTotalItems) {
                isLoading = false;
                previousTotalItems = totalItemCount;
            }
        }
        if (!isLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
        	if(thereIsMoreItems){
        		isLoading = true;
        		progress.setVisibility(View.VISIBLE);
        		loadMoreItems(totalItemCount);
        	}
        }
    }
 
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    	
    }

}