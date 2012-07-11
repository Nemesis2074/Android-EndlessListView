package net.nemesis2074.endless;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;

/**
 * @author Nemesis2074
 */
public abstract class EndlessListActivity<T> extends ListActivity implements OnScrollListener{
	
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
    
    /*++++++++++++++++++++ METODOS ++++++++++++++++++++*/
    
    protected ArrayList<T> getListItems(){
    	return items;
    }
    
    protected void setListItems(ArrayList<T> items){
    	this.items = items;
    }
    
    protected ArrayAdapter<T> getAdapter(){
    	return this.adapter;
    }
    
    protected void setAdapter(ArrayAdapter<T> adapter){
    	if(getListAdapter() == null){
    		this.adapter = adapter;
    		setListAdapter(this.adapter);
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(footer == null){
			LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
    		footer = inflater.inflate(R.layout.list_footer, null, false);
    		progress = (ProgressBar)footer.findViewById(R.id.list_footer_progress);
    		text = (TextView)footer.findViewById(R.id.list_footer_text);
    		text.setText("Loading...");
    	}
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
