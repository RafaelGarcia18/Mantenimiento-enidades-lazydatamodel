/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.controladores;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author rafael
 * @param <T>
 */
public class AbstractLazyModel<T> extends LazyDataModel<T> {

    private final List<T> datasource;

    public AbstractLazyModel(List<T> datasource) {
        this.datasource = datasource;
    }

    /**
     *
     * @param first
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @param filters
     * @return
     */
    @Override
    public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        List<T> data = new ArrayList<T>();

        //filter
        for (T car : datasource) {
            boolean match = true;

            if (filters != null) {
                for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                    try {
                        String filterProperty = it.next();
                        Object filterValue = filters.get(filterProperty);
                        String fieldValue = String.valueOf(car.getClass().getField(filterProperty).get(car));

                        if (filterValue == null || fieldValue.startsWith(filterValue.toString())) {
                            match = true;
                        } else {
                            match = false;
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                        match = false;
                    }
                }
            }

            if (match) {
                data.add(car);
            }
        }

        //sort
//        if (sortField != null) {
//            Collections.sort(data, new LazySorter(sortField, sortOrder));
//        }

        //rowCount
        int dataSize = data.size();
        this.setRowCount(dataSize);

        //paginate
        if (dataSize > pageSize) {
            try {
                return data.subList(first, first + pageSize);
            } catch (IndexOutOfBoundsException e) {
                return data.subList(first, first + (dataSize % pageSize));
            }
        } else {
            return data;
        }
    }

    /**
     * Metodo abstracto para cargar lazydatamodel en muestra app, Sobreeescrobir
     * este metodo por que es unico en cada entidad
     *
     * @param rowKey recive un Integer con el id
     * @return debe de devolver la entidad con el id ingresado si se
     * sobreescribe bien
     */
    @Override
    public T getRowData(String rowKey) {
        for (T car : datasource) {
//            if (car.getIdTipoVehiculo().equals(rowKey)) {
//                return car;
//            }
        }
        return null;
    }

    /**
     * Metodo abstracto para cargar lazydatamodel en muestra app, Sobreeescrobir
     * este metodo por que es unico en cada entidad
     *
     * @param car recive una entidad
     * @return debe de devolver la entidad con la informacion ingresado si se
     * sobreescribe bien
     */
    @Override
    public Object getRowKey(T car) {
        return null;
    }

}
