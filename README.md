Android - EndlessListView
=========================

¿Qué es?
--------

Esta librería surgió de la necesidad de implementar la técnica conocida como
*endless scrolling* o *infinite scrolling* en la plataforma Android. El uso de esta
técnica es muy común en aplicaciones móviles (también en varios sitios web).

La técnica *endless scrolling* consiste en llenar una lista de datos
(en este caso, un *ListView*) en bloques de *n* número de elementos;
al iniciar la aplicación se muestran los primeros *n* elementos de la lista; posteriormente,
cuando el usuario hace scroll y llega al final de la lista; entonces, se agrega
el siguiente bloque de elementos.

Mostrar los datos de esta forma mejora la velocidad de carga de datos y da al usuario una
sensación de fluidez en la aplicación; es recomendable utilizarla en listas de datos muy
largas.

¿Cómo la agrego a mi proyecto?
------------------------------

Desde *Eclipse* puede agregarse *EndlessListView* como proyecto de librería:

1. Crea un nuevo proyecto de Android desde código existente (*Android project from existing source*),
   utilizando la librería como fuente.
   
2. Asegúrate de que el proyecto este compilado utilizando Android 4.0 o superior.

3. Ahora, desde las propiedades de tu proyecto, agrega el proyecto creado a la sección *Libraries*
   de la categoría *Android*.

¿Cómo se utiliza?
-----------------

*EndlessListView* puede implementarse ya sea como *Fragment* o como *Activity*, en ambos
casos la implementación es la misma; la unica diferencia es la clase a utilizar: 
*EndlessListFragment<T>* o *EndlessListActivity<T>*. Los pasos siguientes muestran como
implementar un *Fragment*.

1. Crear el modelo de datos.
    
    El modelo de datos es la clase que define cada uno de los elementos
    que serán representados en el *ListView*. Ejemplo:
    
        public class Item {
	    
            private String title;
            private String description;
            
            public String getTitle() {
                return title;
            }
            
            public void setTitle(String title) {
                this.title = title;
            }
            
            public String getDescription() {
                return description;
            }
            
            public void setDescription(String description) {
                this.description = description;
            }
        }

2. Crear el fragmento que implementará el *endless scrolling*

    Se debe crear un fragmento que herede de la clase genérica *EndlessListFragment<T>*; donde
    *T* es el modelo de datos a utilizar. Ejemplo:

        import net.nemesis2074.endless.EndlessListFragment;
        
        public class MyEndlessListFragment extends EndlessListFragment<Item>{
            
        }
        
3. Inicializar la lista.

    Se debe inicializar el *ListView* indicándole el *Adapter* que debe utilizar. 
    Ejemplo:
    	
        initListView(new ItemsArrayAdapter(context, R.layout.item, getListItems()));

4. Implementar métodos abstractos

    La clase *EndlessListFragment<T>* contiene dos métodos abstractos que deben
    ser implementados:
    
    El método *refreshItems()* se ejecutará para solicitar el primer bloque de elementos
    de la lista o cuando explícitamente se llame al método *refresh()*:
    
        @Override
        public void refreshItems() {
            // Cargar el primer bloque de datos aquí
        }

    El método *loadMoreItems(int totalItems)* se ejecutará cuando el usuario llegue al
    final de la lista y sea necesario cargar el siguiente bloque de elementos; el
    parámetro *totalItems* contiene el número de elementos en la lista.
    
        @Override
        public void loadMoreItems(int totalItems) {
            // Cargar el siguiente bloque de datos aquí
        }

    Dentro de estos métodos se deben cargar los datos desde la fuente deseada (XML, JSON, Base de datos, etc.) y parsearlos
    en el modelo de datos.

5. Agregar los elementos a la lista.

    Finalmente, se deben agregar los elementos al *ListView* por medio de cualquiera de los siguientes métodos:
    *addItems(ArrayList<T> items)*, *addItem(T item)*, *addItemAtLast(T item)*; donde *T* es el modelo de datos utilizado.
    Ejemplo:
    
        @Override
        public void loadMoreItems(int totalItems) {
            // Se cargan los elementos desde alguna fuente de datos.
            ArrayList<Item> items = loadMoreItemsFromAnySource(totalItems);
            // Se agregan los datos a la lista.
            addItems(items);
        }

Licencia
--------

Copyright (c) 2012 Adair Castillo (Nemesis2074)
